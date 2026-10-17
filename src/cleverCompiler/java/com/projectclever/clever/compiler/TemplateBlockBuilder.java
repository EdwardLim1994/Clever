package com.projectclever.clever.compiler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.projectclever.clever.compiler.antlr.CleverTemplateParser;

final class TemplateBlockBuilder {

	TemplateBlock build(String source, CleverTemplateParser.TemplateBodyContext body, String sourceFile) {
		int start = body.LBRACE().getSymbol().getStopIndex() + 1;
		int end = body.RBRACE().getSymbol().getStartIndex();
		String template = source.substring(start, end);
		List<CleverTemplateParser.InterpolationContext> interpolations = new ArrayList<>();
		collectInterpolations(body, interpolations);
		interpolations
				.sort(Comparator.comparingInt(context -> context.INTERPOLATION_START().getSymbol().getStartIndex()));
		StringBuilder marked = new StringBuilder();
		Map<String, TemplateExpression> expressions = new LinkedHashMap<>();
		int cursor = start;
		int index = 0;
		for (CleverTemplateParser.InterpolationContext interpolation : interpolations) {
			Token expressionStart = interpolation.INTERPOLATION_START().getSymbol();
			Token expressionEnd = interpolation.RPAREN().getSymbol();
			marked.append(source, cursor, expressionStart.getStartIndex());
			String marker = "CLEVEREXPRESSION" + index++ + "END";
			while (template.contains(marker) || expressions.containsKey(marker)) {
				marker += "X";
			}
			String expression = source.substring(expressionStart.getStopIndex() + 1, expressionEnd.getStartIndex())
					.trim();
			if (expression.isEmpty()) {
				throw new IllegalArgumentException(sourceFile + ":" + expressionStart.getLine() + ":"
						+ (expressionStart.getCharPositionInLine() + 1) + ": template expression cannot be empty");
			}
			expressions.put(marker, new TemplateExpression(expression, expressionStart));
			marked.append(marker);
			cursor = expressionEnd.getStopIndex() + 1;
		}
		String markedTemplate = marked.append(source, cursor, end).toString();
		return TemplateBlock.builder().body(Jsoup.parseBodyFragment(markedTemplate).body())
				.expressions(expressions).build();
	}

	private static void collectInterpolations(ParseTree tree,
			List<CleverTemplateParser.InterpolationContext> interpolations) {
		if (tree instanceof CleverTemplateParser.InterpolationContext interpolation) {
			interpolations.add(interpolation);
		}
		for (int i = 0; i < tree.getChildCount(); i++) {
			collectInterpolations(tree.getChild(i), interpolations);
		}
	}
}
