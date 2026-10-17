package com.projectclever.clever.compiler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import com.projectclever.clever.compiler.antlr.CleverTemplateLexer;
import com.projectclever.clever.compiler.antlr.CleverTemplateParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

class TemplateJavaGeneratorTests {

	@Test
	void generateBuildsJavaRenderClassFromParsedTemplateParts() {
		String source = "@Page ReusablePage @Code {} @Template { <p>Ready</p> }";
		CleverTemplateParser parser = new CleverTemplateParser(
				new CommonTokenStream(new CleverTemplateLexer(CharStreams.fromString(source))));
		TemplateBlock template = new TemplateBlockBuilder().build(source,
				parser.templateFile().templateBlock().templateBody(), "ReusablePage.clever");
		String generated = new TemplateJavaGenerator().generate("ReusablePage", "com.example", false, List.of(),
				"private int count = 1;", List.of("import java.util.Locale;"), template, false,
				"ReusablePage.clever", source);

		assertTrue(generated.startsWith("package com.example;"));
		assertTrue(generated.contains("import java.util.Locale;"));
		assertTrue(generated.contains("private int count = 1;"));
		assertTrue(generated.contains("HtmlTagGenerator.appendText(element0, \"Ready\")"));
		assertTrue(generated.contains("return root.html();"));
	}
}
