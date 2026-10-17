package com.projectclever.clever.compiler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

class TemplateJavaGeneratorTests {

	@Test
	void generateBuildsJavaRenderClassFromParsedTemplateParts() {
		String generated = new TemplateJavaGenerator().generate("ReusablePage", "com.example", false, "private int count = 1;",
				List.of("import java.util.Locale;"), Jsoup.parseBodyFragment("<p>Ready</p>").body(), Map.of(),
				"ReusablePage.clever", "<p>Ready</p>");

		assertTrue(generated.startsWith("package com.example;"));
		assertTrue(generated.contains("import java.util.Locale;"));
		assertTrue(generated.contains("private int count = 1;"));
		assertTrue(generated.contains("HtmlTagGenerator.appendText(element0, \"Ready\")"));
		assertTrue(generated.contains("return root.html();"));
	}
}
