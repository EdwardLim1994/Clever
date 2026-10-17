package com.projectclever.clever.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.projectclever.clever.compiler.antlr.CleverTemplateLexer;
import com.projectclever.clever.compiler.antlr.CleverTemplateParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

class BlockBuilderTests {

	@Test
	void codeBlockBuilderExtractsImportsAndPreservesJavaMembers() {
		String source = "@Page Example @Code { import java.util.Locale; private String name = \"Ada\"; } "
				+ "@Template { <p>Hi</p> }";
		CommonTokenStream tokens = tokens(source);
		CleverTemplateParser.TemplateFileContext parsed = new CleverTemplateParser(tokens).templateFile();

		CodeBlock code = new CodeBlockBuilder().build(source, parsed.codeBlock().block(), tokens);

		assertEquals("import java.util.Locale;", code.imports().get(0));
		assertTrue(code.source().contains("private String name = \"Ada\";"));
		assertEquals(1, code.imports().size());
	}

	@Test
	void templateBlockBuilderReturnsMarkupAndInterpolationExpressions() {
		String source = "@Page Example @Code {} @Template { <p>Hello @(name)</p> }";
		CleverTemplateParser.TemplateBodyContext body = new CleverTemplateParser(tokens(source))
				.templateFile().templateBlock().templateBody();

		TemplateBlock template = new TemplateBlockBuilder().build(source, body, "Example.clever");

		assertTrue(template.body().html().contains("<p>Hello CLEVEREXPRESSION0END</p>"));
		assertEquals("name", template.expressions().get("CLEVEREXPRESSION0END").source());
	}

	@Test
	void templateBlockBuilderReportsEmptyExpressionLocation() {
		String source = "@Page Example\n@Code {}\n@Template { <p>@()</p> }";
		CleverTemplateParser.TemplateBodyContext body = new CleverTemplateParser(tokens(source))
				.templateFile().templateBlock().templateBody();
		TemplateBlockBuilder builder = new TemplateBlockBuilder();

		IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
				() -> builder.build(source, body, "Example.clever"));

		assertTrue(error.getMessage().contains("Example.clever:3:"));
		assertTrue(error.getMessage().contains("template expression cannot be empty"));
	}

	private static CommonTokenStream tokens(String source) {
		return new CommonTokenStream(new CleverTemplateLexer(CharStreams.fromString(source)));
	}
}
