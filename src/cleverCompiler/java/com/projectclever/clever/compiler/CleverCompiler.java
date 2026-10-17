package com.projectclever.clever.compiler;

import com.projectclever.clever.compiler.antlr.CleverTemplateLexer;
import com.projectclever.clever.compiler.antlr.CleverTemplateParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.lang.model.SourceVersion;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

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
		CleverTemplateParser.TemplateDeclarationContext declaration = parsed.templateDeclaration();
		String className = declaration.IDENTIFIER().getText();
		validateIdentifier(className, templateFile.toString(), parsed.templateDeclaration().IDENTIFIER().getSymbol());
		String kind = declaration.PAGE() != null ? "Page" : declaration.COMPONENT() != null ? "Component" : "Layout";
		List<String> inputs = inputDeclarations(source, parsed.inputsDirective(), kind, templateFile.toString());
		CodeBlock code = new CodeBlockBuilder().build(source, parsed.codeBlock().block(), tokens);
		CleverTemplateParser.TemplateBodyContext templateBody = parsed.templateBlock().templateBody();
		Map<String, TemplateSourceMetadata> referencedTemplates = referencedTemplates(templateFile, templateBody);
		TemplateBlock template = new TemplateBlockBuilder().build(source, templateBody,
				templateFile.toString(), referencedTemplates);
		if (!template.slots().isEmpty() && !kind.equals("Layout")) {
			throw new IllegalArgumentException(templateFile + ": @Slot declarations are only valid in a layout template");
		}
		return new TemplateJavaGenerator().generate(className, packageName, parsed.REQUIRED_ARGS_CONSTRUCTOR() != null,
				inputs,
				code.source(), code.imports(),
				template, kind.equals("Layout"), templateFile.toString(), source);
	}

	private static Map<String, TemplateSourceMetadata> referencedTemplates(Path templateFile,
			CleverTemplateParser.TemplateBodyContext templateBody) throws IOException {
		Map<String, String> referencedTypes = new HashMap<>();
		collectReferencedTypes(templateBody, referencedTypes);
		if (referencedTypes.isEmpty()) {
			return Map.of();
		}
		Map<String, TemplateSourceMetadata> metadataByType = new HashMap<>();
		try (Stream<Path> files = Files.walk(templateFile.toAbsolutePath().getParent())) {
			for (Path candidate : files.filter(path -> path.toString().endsWith(".clever")).toList()) {
				String candidateSource = Files.readString(candidate, StandardCharsets.UTF_8);
				CleverTemplateLexer candidateLexer = new CleverTemplateLexer(CharStreams.fromString(candidateSource));
				candidateLexer.removeErrorListeners();
				CommonTokenStream candidateTokens = new CommonTokenStream(candidateLexer);
				CleverTemplateParser candidateParser = new CleverTemplateParser(candidateTokens);
				candidateParser.removeErrorListeners();
				CleverTemplateParser.TemplateFileContext candidateTemplate = candidateParser.templateFile();
				if (candidateParser.getNumberOfSyntaxErrors() > 0) {
					continue;
				}
				CleverTemplateParser.TemplateDeclarationContext candidateDeclaration = candidateTemplate.templateDeclaration();
				String templateName = candidateDeclaration.IDENTIFIER().getText();
				String candidateKind = candidateDeclaration.COMPONENT() != null ? "Component"
						: candidateDeclaration.LAYOUT() != null ? "Layout" : "Page";
				if (referencedTypes.get(templateName) != null && referencedTypes.get(templateName).equals(candidateKind)) {
					if (metadataByType.containsKey(templateName)) {
						throw new IllegalArgumentException(templateFile + ": ambiguous referenced template '" + templateName + "'");
					}
					List<String> inputs = new ArrayList<>();
					if (candidateTemplate.inputsDirective() != null) {
						for (CleverTemplateParser.InputParameterContext input : candidateTemplate.inputsDirective().inputParameter()) {
							var type = input.javaType();
							inputs.add(candidateSource.substring(type.getStart().getStartIndex(),
									type.getStop().getStopIndex() + 1).trim());
						}
					}
					Set<String> fields = Set.of();
					Set<String> slots = Set.of();
					if (candidateKind.equals("Component")) {
						CodeBlock componentCode = new CodeBlockBuilder().build(candidateSource,
								candidateTemplate.codeBlock().block(), candidateTokens);
						fields = ComponentTemplateMetadata.publicFields(componentCode.source());
					} else if (candidateKind.equals("Layout")) {
						TemplateBlock layoutTemplate = new TemplateBlockBuilder().build(candidateSource,
								candidateTemplate.templateBlock().templateBody(), candidate.toString());
						slots = Set.copyOf(layoutTemplate.slots().values());
					}
					metadataByType.put(templateName, new TemplateSourceMetadata(List.copyOf(inputs), fields, slots));
				}
			}
		}
		return Map.copyOf(metadataByType);
	}

	private static void collectReferencedTypes(ParseTree tree, Map<String, String> types) {
		if (tree instanceof CleverTemplateParser.ComponentInvocationContext component) {
			types.put(simpleName(component.qualifiedName().getText()), "Component");
		} else if (tree instanceof CleverTemplateParser.LayoutInvocationContext layout) {
			types.put(simpleName(layout.qualifiedName().getText()), "Layout");
		}
		for (int i = 0; i < tree.getChildCount(); i++) {
			collectReferencedTypes(tree.getChild(i), types);
		}
	}

	private static String simpleName(String type) {
		int separator = type.lastIndexOf('.');
		return separator < 0 ? type : type.substring(separator + 1);
	}

	private static List<String> inputDeclarations(String source, CleverTemplateParser.InputsDirectiveContext directive,
			String kind, String sourceFile) {
		if (directive == null) {
			return List.of();
		}
		if (kind.equals("Page")) {
			throw new IllegalArgumentException(sourceFile + ": @Inputs can only be declared by a component or layout");
		}
		List<String> inputs = new ArrayList<>();
		Set<String> names = new HashSet<>();
		for (CleverTemplateParser.InputParameterContext parameter : directive.inputParameter()) {
			String name = parameter.IDENTIFIER().getText();
			Token nameToken = parameter.IDENTIFIER().getSymbol();
			if (!SourceVersion.isIdentifier(name) || SourceVersion.isKeyword(name, SourceVersion.latestSupported())) {
				throw new IllegalArgumentException(sourceFile + ":" + nameToken.getLine() + ":"
						+ (nameToken.getCharPositionInLine() + 1) + ": invalid input name '" + name + "'");
			}
			if (!names.add(name)) {
				throw new IllegalArgumentException(sourceFile + ":" + nameToken.getLine() + ":"
						+ (nameToken.getCharPositionInLine() + 1) + ": duplicate input name '" + name + "'");
			}
			var type = parameter.javaType();
			String typeName = source.substring(type.getStart().getStartIndex(), type.getStop().getStopIndex() + 1).trim();
			inputs.add(typeName + " " + name);
		}
		return inputs;
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
