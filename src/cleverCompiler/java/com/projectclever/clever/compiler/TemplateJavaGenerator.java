package com.projectclever.clever.compiler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

final class TemplateJavaGenerator {

	String generate(String className, String packageName, boolean requiredArgsConstructor, List<String> inputs,
			String code, List<String> imports, TemplateBlock template, boolean layout, String sourceFile, String source) {
		StringBuilder generated = new StringBuilder();
		boolean constructorRequired = requiredArgsConstructor && inputs.isEmpty();
		if (!packageName.isBlank()) {
			generated.append("package ").append(packageName).append(";\n\n");
		}
		imports.forEach(importLine -> generated.append(importLine).append('\n'));
		generated.append("import com.projectclever.clever.runtime.HtmlTagGenerator;\n")
				.append(constructorRequired ? "import lombok.RequiredArgsConstructor;\n" : "")
				.append("import org.jsoup.Jsoup;\n")
				.append("import org.jsoup.nodes.Element;\n")
				.append("import java.util.Map;\n")
				.append("import java.util.Set;\n")
				.append("import java.util.function.Supplier;\n\n")
				.append(constructorRequired ? "@RequiredArgsConstructor\n" : "")
				.append("public class ").append(className).append(" {\n");
		for (String input : inputs) {
			int separator = input.lastIndexOf(' ');
			generated.append("\tprivate final ").append(input, 0, separator).append(' ')
					.append(input.substring(separator + 1)).append(";\n");
		}
		if (!inputs.isEmpty()) {
			generated.append("\tpublic ").append(className).append('(').append(String.join(", ", inputs)).append(") {\n");
			for (String input : inputs) {
				String name = input.substring(input.lastIndexOf(' ') + 1);
				generated.append("\t\tthis.").append(name).append(" = ").append(name).append(";\n");
			}
			generated.append("\t}\n");
		}
		generated.append(code).append('\n');
		if (layout) {
			generated.append("\tpublic String render() { return render(Map.of()); }\n")
					.append("\tpublic String render(Map<String, Supplier<String>> slots) {\n")
					.append("\t\tif (!Set.of(")
					.append(String.join(", ", template.slots().values().stream().map(TemplateJavaGenerator::javaString).toList()))
					.append(").containsAll(slots.keySet())) throw new IllegalArgumentException(\"unknown layout slot assignment\");\n");
		} else {
			generated.append("\tpublic String render() {\n");
		}
		generated.append("\t\tElement root = Jsoup.parseBodyFragment(\"\").body();\n")
				.append("\t\troot.ownerDocument().outputSettings().prettyPrint(false);\n");
		appendNodes("root", template, generated, new int[] {0}, new LinkedHashMap<>(), layout, sourceFile, source);
		generated.append("\t\treturn root.html();\n\t}\n}\n");
		return generated.toString();
	}

	private static void appendNodes(String parentVariable, TemplateBlock template,
			StringBuilder generated, int[] elementIndex, Map<String, String> exportedFields,
			boolean layout, String sourceFile, String source) {
		for (Node node : template.body().childNodes()) {
			if (node instanceof TextNode textNode) {
				appendText(parentVariable, textNode.getWholeText(), template, generated, elementIndex, exportedFields,
						layout, sourceFile, source);
			} else if (node instanceof Element child) {
				appendElement(parentVariable, child, template, generated, elementIndex, exportedFields,
						layout, sourceFile, source);
			} else if (node instanceof Comment comment) {
				generated.append("\t\t").append(parentVariable).append(".append(")
						.append(javaString(comment.outerHtml())).append(");\n");
			} else {
				generated.append("\t\t").append(parentVariable).append(".append(")
						.append(javaString(node.outerHtml())).append(");\n");
			}
		}
	}

	private static void appendElement(String parentVariable, Element element, TemplateBlock template,
			StringBuilder generated, int[] elementIndex, Map<String, String> exportedFields,
			boolean layout, String sourceFile, String source) {
		for (org.jsoup.nodes.Attribute attribute : element.attributes()) {
			TemplateExpression expression = expressionIn(attribute.getValue(), template.expressions());
			if (expression != null) {
				throw diagnostic(sourceFile, expression.location(), "expressions in HTML attributes are not supported yet");
			}
			if (attribute.getValue().contains("@(")) {
				int offset = source.indexOf(attribute.getValue());
				throw diagnosticAt(sourceFile, source, Math.max(offset, 0) + attribute.getValue().indexOf("@("),
						"expressions in HTML attributes are not supported yet");
			}
		}
		String variable = "element" + elementIndex[0]++;
		generated.append("\t\tElement ").append(variable).append(" = HtmlTagGenerator.element(")
				.append(javaString(element.tagName())).append(", ").append(attributes(element)).append(");\n")
				.append("\t\t").append(parentVariable).append(".appendChild(").append(variable).append(");\n");
		appendNodes(variable, templateForElement(element, template), generated, elementIndex, exportedFields,
				layout, sourceFile, source);
	}

	private static TemplateBlock templateForElement(Element element, TemplateBlock parent) {
		return TemplateBlock.builder().body(element).expressions(parent.expressions()).components(parent.components())
				.componentExports(parent.componentExports()).layouts(parent.layouts()).slots(parent.slots()).build();
	}

	private static String attributes(Element element) {
		if (element.attributes().isEmpty()) {
			return "Map.of()";
		}
		List<String> entries = new ArrayList<>();
		for (org.jsoup.nodes.Attribute attribute : element.attributes()) {
			entries.add("Map.entry(" + javaString(attribute.getKey()) + ", " + javaString(attribute.getValue()) + ")");
		}
		return "Map.ofEntries(" + String.join(", ", entries) + ")";
	}

	private static void appendText(String parentVariable, String text, TemplateBlock template,
			StringBuilder generated, int[] elementIndex, Map<String, String> exportedFields,
			boolean layout, String sourceFile, String source) {
		int cursor = 0;
		while (cursor < text.length()) {
			String marker = null;
			int markerStart = text.length();
			for (String candidate : markers(template)) {
				int found = text.indexOf(candidate, cursor);
				if (found >= 0 && found < markerStart) {
					marker = candidate;
					markerStart = found;
				}
			}
			if (marker == null) {
				appendTextCall(parentVariable, text.substring(cursor), generated);
				return;
			}
			appendTextCall(parentVariable, text.substring(cursor, markerStart), generated);
			if (template.expressions().containsKey(marker)) {
				generated.append("\t\tHtmlTagGenerator.appendText(").append(parentVariable).append(", ")
						.append(rewriteExports(template.expressions().get(marker).source(), exportedFields)).append(");\n");
			} else if (template.components().containsKey(marker)) {
				Set<String> fields = template.componentExports().getOrDefault(marker, Set.of());
				if (fields.isEmpty()) {
					generated.append("\t\t").append(parentVariable).append(".append(")
							.append(template.components().get(marker)).append(".render());\n");
				} else {
					String component = "component" + elementIndex[0]++;
					generated.append("\t\tvar ").append(component).append(" = ")
							.append(template.components().get(marker)).append(";\n")
							.append("\t\t").append(parentVariable).append(".append(").append(component)
							.append(".render());\n");
					fields.forEach(field -> exportedFields.put(field, component));
				}
			} else if (template.layouts().containsKey(marker)) {
				appendLayout(parentVariable, template.layouts().get(marker), generated, elementIndex, sourceFile, source);
			} else if (layout && template.slots().containsKey(marker)) {
				generated.append("\t\t").append(parentVariable).append(".append(slots.getOrDefault(")
						.append(javaString(template.slots().get(marker))).append(", () -> \"\").get());\n");
			} else if (template.slots().containsKey(marker)) {
				throw new IllegalArgumentException(sourceFile + ": " + "@Slot declarations are only valid in a layout template");
			}
			cursor = markerStart + marker.length();
		}
	}

	private static List<String> markers(TemplateBlock template) {
		List<String> markers = new ArrayList<>();
		markers.addAll(template.expressions().keySet());
		markers.addAll(template.components().keySet());
		markers.addAll(template.layouts().keySet());
		markers.addAll(template.slots().keySet());
		return markers;
	}

	private static void appendLayout(String parentVariable, TemplateLayoutInvocation layout,
			StringBuilder generated, int[] elementIndex, String sourceFile, String source) {
		generated.append("\t\t").append(parentVariable).append(".append(")
				.append(layout.constructor()).append(".render(");
		if (layout.slots().isEmpty()) {
			generated.append("Map.of()");
		} else {
			List<String> slotEntries = new ArrayList<>();
			for (Map.Entry<String, TemplateBlock> slot : layout.slots().entrySet()) {
				int index = elementIndex[0]++;
				String slotRoot = "slotRoot" + index;
				StringBuilder render = new StringBuilder("() -> {\n")
						.append("\t\t\tElement ").append(slotRoot).append(" = Jsoup.parseBodyFragment(\"\").body();\n")
						.append("\t\t\t").append(slotRoot).append(".ownerDocument().outputSettings().prettyPrint(false);\n");
				appendNodes(slotRoot, slot.getValue(), render, elementIndex, new LinkedHashMap<>(), false, sourceFile, source);
				render.append("\t\t\treturn ").append(slotRoot).append(".html();\n\t\t}");
				slotEntries.add("Map.entry(" + javaString(slot.getKey()) + ", " + render + ")");
			}
			generated.append("Map.<String, Supplier<String>>ofEntries(")
					.append(String.join(", ", slotEntries)).append(')');
		}
		generated.append("));\n");
	}

	private static void appendTextCall(String parentVariable, String text, StringBuilder generated) {
		if (!text.isEmpty()) {
			generated.append("\t\tHtmlTagGenerator.appendText(").append(parentVariable).append(", ")
					.append(javaString(text)).append(");\n");
		}
	}

	private static TemplateExpression expressionIn(String text, Map<String, TemplateExpression> expressions) {
		return expressions.entrySet().stream().filter(entry -> text.contains(entry.getKey()))
				.map(Map.Entry::getValue).findFirst().orElse(null);
	}

	private static String rewriteExports(String expression, Map<String, String> exportedFields) {
		if (exportedFields.isEmpty()) {
			return expression;
		}
		// shortcut: exported names are rewritten lexically until Java expression scope resolution exists.
		StringBuilder rewritten = new StringBuilder();
		for (int index = 0; index < expression.length();) {
			char current = expression.charAt(index);
			if (current == '\'' || current == '"') {
				char quote = current;
				int end = index + 1;
				while (end < expression.length()) {
					char character = expression.charAt(end++);
					if (character == '\\' && end < expression.length()) {
						end++;
					} else if (character == quote) {
						break;
					}
				}
				rewritten.append(expression, index, end);
				index = end;
			} else if (Character.isJavaIdentifierStart(current)) {
				int end = index + 1;
				while (end < expression.length() && Character.isJavaIdentifierPart(expression.charAt(end))) {
					end++;
				}
				String identifier = expression.substring(index, end);
				String component = exportedFields.get(identifier);
				int previous = index - 1;
				while (previous >= 0 && Character.isWhitespace(expression.charAt(previous))) {
					previous--;
				}
				boolean qualified = previous >= 0 && expression.charAt(previous) == '.';
				rewritten.append(component == null || qualified ? identifier : component + "." + identifier);
				index = end;
			} else {
				rewritten.append(current);
				index++;
			}
		}
		return rewritten.toString();
	}

	private static String javaString(String value) {
		return "\"" + StringEscapeUtils.escapeJava(value) + "\"";
	}

	private static IllegalArgumentException diagnostic(String sourceFile, org.antlr.v4.runtime.Token token, String message) {
		return new IllegalArgumentException(sourceFile + ":" + token.getLine() + ":"
				+ (token.getCharPositionInLine() + 1) + ": " + message);
	}

	private static IllegalArgumentException diagnosticAt(String sourceFile, String source, int offset, String message) {
		int line = 1;
		int column = 1;
		for (int i = 0; i < offset; i++) {
			if (source.charAt(i) == '\n') {
				line++;
				column = 1;
			} else {
				column++;
			}
		}
		return new IllegalArgumentException(sourceFile + ":" + line + ":" + column + ": " + message);
	}
}
