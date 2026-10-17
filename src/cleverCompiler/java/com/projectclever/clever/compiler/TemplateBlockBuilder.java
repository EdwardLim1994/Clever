package com.projectclever.clever.compiler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import com.projectclever.clever.compiler.antlr.CleverTemplateParser;

final class TemplateBlockBuilder {

	TemplateBlock build(String source, CleverTemplateParser.TemplateBodyContext body, String sourceFile) {
		return build(source, body, sourceFile, Map.of());
	}

	TemplateBlock build(String source, CleverTemplateParser.TemplateBodyContext body, String sourceFile,
			Map<String, TemplateSourceMetadata> referencedTemplates) {
		int start = body.LBRACE().getSymbol().getStopIndex() + 1;
		int end = body.RBRACE().getSymbol().getStartIndex();
		return buildRange(source, start, end, body.templateContent(), sourceFile, referencedTemplates);
	}

	private TemplateBlock buildRange(String source, int start, int end,
			List<? extends ParseTree> content, String sourceFile,
			Map<String, TemplateSourceMetadata> referencedTemplates) {
		String template = source.substring(start, end);
		List<ParserRuleContext> directives = new ArrayList<>();
		content.forEach(node -> collectDirectives(node, directives));
		directives.sort(Comparator.comparingInt(node -> node.getStart().getStartIndex()));

		StringBuilder marked = new StringBuilder();
		Map<String, TemplateExpression> expressions = new LinkedHashMap<>();
		Map<String, String> components = new LinkedHashMap<>();
		Map<String, Set<String>> componentExports = new LinkedHashMap<>();
		Map<String, TemplateLayoutInvocation> layouts = new LinkedHashMap<>();
		Map<String, String> slots = new LinkedHashMap<>();
		Set<String> slotNames = new HashSet<>();
		Map<String, Token> exportedFields = new LinkedHashMap<>();
		int cursor = start;
		int index = 0;
		for (ParserRuleContext directive : directives) {
			Token first = directive.getStart();
			Token last = directive.getStop();
			int directiveStart = first.getStartIndex();
			int directiveEnd = last.getStopIndex() + 1;
			if (directiveStart < cursor) {
				continue;
			}
			marked.append(source, cursor, directiveStart);
			String marker = marker(template, index++);
			if (directive instanceof CleverTemplateParser.InterpolationContext interpolation) {
				Token expressionStart = interpolation.INTERPOLATION_START().getSymbol();
				Token expressionEnd = interpolation.RPAREN().getSymbol();
				String expression = source.substring(expressionStart.getStopIndex() + 1, expressionEnd.getStartIndex())
						.trim();
				if (expression.isEmpty()) {
					throw diagnostic(sourceFile, expressionStart, "template expression cannot be empty");
				}
				expressions.put(marker, new TemplateExpression(expression, expressionStart));
			} else if (directive instanceof CleverTemplateParser.ComponentInvocationContext component) {
				String type = component.qualifiedName().getText();
				TemplateSourceMetadata metadata = referencedTemplates.get(simpleName(type));
				int argumentCount = component.javaArgumentList() == null ? 0 : component.javaArgumentList().javaExpression().size();
				if (metadata != null && argumentCount != metadata.inputs().size()) {
					throw diagnostic(sourceFile, component.getStart(), "component '" + simpleName(type) + "' expects "
							+ metadata.inputs().size() + " input(s) but received " + argumentCount);
				}
				components.put(marker, constructor(source, type, component.javaArgumentList()));
				Set<String> fields = metadata == null ? Set.of() : metadata.publicFields();
				for (String field : fields) {
					if (exportedFields.containsKey(field)) {
						throw diagnostic(sourceFile, component.getStart(), "ambiguous component export '" + field + "'");
					}
					exportedFields.put(field, component.getStart());
				}
				componentExports.put(marker, fields);
			} else if (directive instanceof CleverTemplateParser.LayoutInvocationContext layout) {
				layouts.put(marker, layoutInvocation(source, layout, sourceFile, referencedTemplates));
			} else if (directive instanceof CleverTemplateParser.SlotDeclarationContext slot) {
				String name = slotName(slot.QUOTED().getSymbol(), sourceFile);
				if (!slotNames.add(name)) {
					throw diagnostic(sourceFile, slot.getStart(), "duplicate layout slot declaration '" + name + "'");
				}
				slots.put(marker, name);
			}
			marked.append(marker);
			cursor = directiveEnd;
		}
		String markedTemplate = marked.append(source, cursor, end).toString();
		return TemplateBlock.builder().body(Jsoup.parseBodyFragment(markedTemplate).body())
				.expressions(expressions).components(components).componentExports(componentExports)
				.layouts(layouts).slots(slots).build();
	}

	private TemplateLayoutInvocation layoutInvocation(String source,
			CleverTemplateParser.LayoutInvocationContext layout, String sourceFile,
			Map<String, TemplateSourceMetadata> referencedTemplates) {
		Map<String, TemplateBlock> slots = new LinkedHashMap<>();
		String layoutName = simpleName(layout.qualifiedName().getText());
		TemplateSourceMetadata metadata = referencedTemplates.get(layoutName);
		int argumentCount = layout.javaArgumentList() == null ? 0 : layout.javaArgumentList().javaExpression().size();
		if (metadata != null && argumentCount != metadata.inputs().size()) {
			throw diagnostic(sourceFile, layout.getStart(), "layout '" + layoutName + "' expects "
					+ metadata.inputs().size() + " input(s) but received " + argumentCount);
		}
		for (CleverTemplateParser.SlotAssignmentContext assignment : layout.slotAssignment()) {
			String name = slotName(assignment.QUOTED().getSymbol(), sourceFile);
			if (metadata != null && !metadata.layoutSlots().contains(name)) {
				throw diagnostic(sourceFile, assignment.getStart(), "layout '" + layoutName + "' does not declare slot '" + name + "'");
			}
			if (slots.containsKey(name)) {
				throw diagnostic(sourceFile, assignment.getStart(), "duplicate layout slot assignment '" + name + "'");
			}
			int start = assignment.LBRACE().getSymbol().getStopIndex() + 1;
			int end = assignment.RBRACE().getSymbol().getStartIndex();
			slots.put(name, buildRange(source, start, end, assignment.templateContent(), sourceFile, referencedTemplates));
		}
		return new TemplateLayoutInvocation(constructor(source, layout.qualifiedName().getText(), layout.javaArgumentList()), slots);
	}

	private static String constructor(String source, String type, CleverTemplateParser.JavaArgumentListContext arguments) {
		if (arguments == null) {
			return "new " + type + "()";
		}
		List<String> values = arguments.javaExpression().stream()
				.map(expression -> source.substring(expression.getStart().getStartIndex(), expression.getStop().getStopIndex() + 1)
						.trim())
				.toList();
		return "new " + type + "(" + String.join(", ", values) + ")";
	}

	private static String slotName(Token quoted, String sourceFile) {
		String literal = quoted.getText();
		String name = literal.substring(1, literal.length() - 1);
		if (name.isBlank()) {
			throw diagnostic(sourceFile, quoted, "slot name cannot be empty");
		}
		return name;
	}

	private static String marker(String template, int index) {
		String marker = "CLEVERDIRECTIVE" + index + "END";
		while (template.contains(marker)) {
			marker += "X";
		}
		return marker;
	}

	private static String simpleName(String type) {
		int separator = type.lastIndexOf('.');
		return separator < 0 ? type : type.substring(separator + 1);
	}

	private static void collectDirectives(ParseTree tree, List<ParserRuleContext> directives) {
		if (tree instanceof CleverTemplateParser.InterpolationContext
				|| tree instanceof CleverTemplateParser.ComponentInvocationContext
				|| tree instanceof CleverTemplateParser.LayoutInvocationContext
				|| tree instanceof CleverTemplateParser.SlotDeclarationContext) {
			directives.add((ParserRuleContext) tree);
			return;
		}
		for (int i = 0; i < tree.getChildCount(); i++) {
			collectDirectives(tree.getChild(i), directives);
		}
	}

	private static IllegalArgumentException diagnostic(String sourceFile, Token token, String message) {
		return new IllegalArgumentException(sourceFile + ":" + token.getLine() + ":"
				+ (token.getCharPositionInLine() + 1) + ": " + message);
	}
}
