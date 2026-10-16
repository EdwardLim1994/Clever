package com.projectclever.clever.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.Token;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

final class TemplateJavaGenerator {

	String generate(String className, String packageName, boolean requiredArgsConstructor, String code, List<String> imports, Element body,
			Map<String, TemplateExpression> expressions, String sourceFile, String source) {
		StringBuilder generated = new StringBuilder();
		if (!packageName.isBlank()) {
			generated.append("package ").append(packageName).append(";\n\n");
		}
		imports.forEach(importLine -> generated.append(importLine).append('\n'));
		generated.append("import com.projectclever.clever.runtime.HtmlTagGenerator;\n")
				.append(requiredArgsConstructor ? "import lombok.RequiredArgsConstructor;\n" : "")
				.append("import org.jsoup.Jsoup;\n")
				.append("import org.jsoup.nodes.Element;\n")
				.append("import java.util.Map;\n\n")
					.append(requiredArgsConstructor ? "@RequiredArgsConstructor\n" : "")
					.append("public class ").append(className).append(" {\n")
				.append(code).append("\n\tpublic String render() {\n")
				.append("\t\tElement root = Jsoup.parseBodyFragment(\"\").body();\n")
				.append("\t\troot.ownerDocument().outputSettings().prettyPrint(false);\n");
		appendNodes(body, "root", expressions, generated, new int[] {0}, sourceFile, source);
		return generated.append("\t\treturn root.html();\n\t}\n}\n").toString();
	}

	private static void appendNodes(Element parent, String parentVariable,
			Map<String, TemplateExpression> expressions, StringBuilder generated, int[] elementIndex,
			String sourceFile, String source) {
		for (Node node : parent.childNodes()) {
			if (node instanceof TextNode textNode) {
				appendText(parentVariable, textNode.getWholeText(), expressions, generated);
			} else if (node instanceof Element child) {
				appendElement(parentVariable, child, expressions, generated, elementIndex, sourceFile, source);
			} else if (node instanceof Comment comment) {
				generated.append("\t\t").append(parentVariable).append(".append(")
						.append(javaString(comment.outerHtml())).append(");\n");
			} else {
				generated.append("\t\t").append(parentVariable).append(".append(")
						.append(javaString(node.outerHtml())).append(");\n");
			}
		}
	}

	private static void appendElement(String parentVariable, Element element,
			Map<String, TemplateExpression> expressions, StringBuilder generated, int[] elementIndex,
			String sourceFile, String source) {
		for (org.jsoup.nodes.Attribute attribute : element.attributes()) {
			TemplateExpression expression = expressionIn(attribute.getValue(), expressions);
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
		appendNodes(element, variable, expressions, generated, elementIndex, sourceFile, source);
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

	private static void appendText(String parentVariable, String text, Map<String, TemplateExpression> expressions,
			StringBuilder generated) {
		int cursor = 0;
		while (cursor < text.length()) {
			String marker = null;
			int markerStart = text.length();
			for (String candidate : expressions.keySet()) {
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
			TemplateExpression expression = expressions.get(marker);
			generated.append("\t\tHtmlTagGenerator.appendText(").append(parentVariable).append(", ")
					.append(expression.source()).append(");\n");
			cursor = markerStart + marker.length();
		}
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

	private static String javaString(String value) {
		return "\"" + StringEscapeUtils.escapeJava(value) + "\"";
	}

	private static IllegalArgumentException diagnostic(String sourceFile, Token token, String message) {
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
