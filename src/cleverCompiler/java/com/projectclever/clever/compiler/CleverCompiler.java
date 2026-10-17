package com.projectclever.clever.compiler;

import com.projectclever.clever.compiler.antlr.CleverTemplateLexer;
import com.projectclever.clever.compiler.antlr.CleverTemplateParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.lang.model.SourceVersion;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public final class CleverCompiler {

	public String compile(Path templateFile, String packageName) throws IOException {
		packageName = packageName == null ? "" : packageName;
		if (!templateFile.toString().endsWith(".clever")) {
			throw new IllegalArgumentException(templateFile + ": expected a .clever template file");
		}
		validatePackageName(packageName, templateFile.toString());

		String source = Files.readString(templateFile, StandardCharsets.UTF_8);
		CleverTemplateLexer lexer = new CleverTemplateLexer(CharStreams.fromString(source));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CleverTemplateParser parser = new CleverTemplateParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(new TemplateSyntaxErrorListener(templateFile));

		CleverTemplateParser.TemplateFileContext parsed = parser.templateFile();
		String className = parsed.templateDeclaration().IDENTIFIER().getText();
		validateIdentifier(className, templateFile.toString(), parsed.templateDeclaration().IDENTIFIER().getSymbol());
		CodeBlock code = new CodeBlockBuilder().build(source, parsed.codeBlock().block(), tokens);
		TemplateBlock template = new TemplateBlockBuilder().build(source,
				parsed.templateBlock().templateBody(), templateFile.toString());
		return new TemplateJavaGenerator().generate(className, packageName, parsed.REQUIRED_ARGS_CONSTRUCTOR() != null,
				code.source(), code.imports(),
				template.body(), template.expressions(), templateFile.toString(), source);
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			throw new IllegalArgumentException("Usage: CleverCompiler <input.clever> <package> <output.java>");
		}
		Path outputFile = Path.of(args[2]);
		Path outputDirectory = outputFile.getParent();
		if (outputDirectory != null) {
			Files.createDirectories(outputDirectory);
		}
		Files.writeString(outputFile, new CleverCompiler().compile(Path.of(args[0]), args[1]), StandardCharsets.UTF_8);
	}

	private static void validatePackageName(String packageName, String sourceFile) {
		if (packageName == null || packageName.isBlank()) {
			return;
		}
		for (String part : packageName.split("\\.", -1)) {
			if (!SourceVersion.isIdentifier(part) || SourceVersion.isKeyword(part, SourceVersion.latestSupported())) {
				throw new IllegalArgumentException(sourceFile + ": invalid Java package name '" + packageName + "'");
			}
		}
	}

	private static void validateIdentifier(String identifier, String sourceFile, Token token) {
		if (!SourceVersion.isIdentifier(identifier) || SourceVersion.isKeyword(identifier, SourceVersion.latestSupported())) {
			throw new IllegalArgumentException(sourceFile + ":" + token.getLine() + ":"
					+ (token.getCharPositionInLine() + 1) + ": invalid Java class name '" + identifier + "'");
		}
	}

}
