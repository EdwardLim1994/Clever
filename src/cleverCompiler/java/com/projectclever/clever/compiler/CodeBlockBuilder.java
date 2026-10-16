package com.projectclever.clever.compiler;

import com.projectclever.clever.compiler.antlr.CleverTemplateLexer;
import com.projectclever.clever.compiler.antlr.CleverTemplateParser;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

final class CodeBlockBuilder {

	CodeBlock build(String source, CleverTemplateParser.BlockContext block, CommonTokenStream tokens) {
		int start = block.LBRACE().getSymbol().getStopIndex() + 1;
		int end = block.RBRACE().getSymbol().getStartIndex();
		String code = source.substring(start, end);
		List<String> imports = new ArrayList<>();
		List<int[]> ranges = new ArrayList<>();
		int depth = 0;
		int firstToken = block.LBRACE().getSymbol().getTokenIndex() + 1;
		int lastToken = block.RBRACE().getSymbol().getTokenIndex();
		for (int i = firstToken; i < lastToken; i++) {
			Token token = tokens.get(i);
			if (token.getType() == CleverTemplateLexer.LBRACE) {
				depth++;
			} else if (token.getType() == CleverTemplateLexer.RBRACE) {
				depth--;
			} else if (depth == 0 && token.getType() == CleverTemplateLexer.IMPORT) {
				for (int endToken = i + 1; endToken < lastToken; endToken++) {
					if (tokens.get(endToken).getType() == CleverTemplateLexer.SEMICOLON) {
						int startOffset = token.getStartIndex() - start;
						int endOffset = tokens.get(endToken).getStopIndex() + 1 - start;
						imports.add(code.substring(startOffset, endOffset).trim());
						ranges.add(new int[] {startOffset, endOffset});
						i = endToken;
						break;
					}
				}
			}
		}
		StringBuilder remaining = new StringBuilder();
		int cursor = 0;
		for (int[] range : ranges) {
			remaining.append(code, cursor, range[0]);
			cursor = range[1];
		}
		return CodeBlock.builder().source(remaining.append(code.substring(cursor)).toString()).imports(imports).build();
	}
}
