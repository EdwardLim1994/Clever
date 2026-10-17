package com.projectclever.clever.compiler;

import java.nio.file.Path;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

final class TemplateSyntaxErrorListener extends BaseErrorListener {
	private final Path templateFile;

	TemplateSyntaxErrorListener(Path templateFile) {
		this.templateFile = templateFile;
	}

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String message, RecognitionException exception) {
		throw new IllegalArgumentException(templateFile + ":" + line + ":" + (charPositionInLine + 1)
				+ ": " + message, exception);
	}
}
