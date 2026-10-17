package com.projectclever.clever.compiler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.projectclever.clever.runtime.HtmlTagGenerator;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.tools.ToolProvider;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CleverCompilerTests {

	@TempDir
	Path tempDir;

	private final CleverCompiler compiler = new CleverCompiler();

	@Test
	void compileAddsLombokConstructorAnnotation() throws IOException {
		Path template = writeTemplate("LombokPage.clever", """
				@Page LombokPage
				@RequiredArgsConstructor
				@Code { private final String name; }
				@Template { <p>@(name)</p> }
				""");

		String generated = compiler.compile(template, "com.example");

		assertTrue(generated.contains("import lombok.RequiredArgsConstructor;"));
		assertTrue(generated.contains("@RequiredArgsConstructor\npublic class LombokPage"));
	}

	@Test
	void compileGeneratesClassWithCodeMembersAndEscapedTemplateExpressions() throws IOException {
		Path template = writeTemplate("GreetingPage.clever", """
				@Page GreetingPage
				@Code {
				    import java.util.Locale;
				    private String name = \"<Alice & Bob>\";
				    private String greeting() { return name.toUpperCase(Locale.ROOT); }
				}
				@Template {
				    <main class=\"greeting\"><h1>Hello @(greeting())!</h1><p>@(name)</p></main>
				}
				""");

		String generated = compiler.compile(template, "com.example.pages");

		assertTrue(generated.startsWith("package com.example.pages;"));
		assertTrue(generated.contains("import java.util.Locale;"));
		assertTrue(generated.contains("private String name = \"<Alice & Bob>\";"));
		assertTrue(generated.contains("HtmlTagGenerator.element(\"main\", Map.ofEntries(Map.entry(\"class\", \"greeting\")))"));
		assertTrue(generated.contains("HtmlTagGenerator.appendText(element1, greeting())"));
		assertTrue(generated.contains("HtmlTagGenerator.appendText(element2, name)"));
		assertTrue(generated.contains("return root.html();"));
	}

	@Test
	void compilePreservesStaticMarkupAndNestedCodeBraces() throws IOException {
		Path template = writeTemplate("Panel.clever", """
				@Component Panel
				@Code {
				    private String value() { return \"}\"; }
				}
				@Template { <section><p>Static text</p></section> }
				""");

		String generated = compiler.compile(template, "");

		assertTrue(generated.contains("public class Panel {"));
		assertTrue(generated.contains("private String value() { return \"}\"; }"));
		assertTrue(generated.contains("HtmlTagGenerator.appendText(element1, \"Static text\")"));
	}

	@Test
	void compileReportsMalformedTemplateWithSourceLocation() throws IOException {
		Path template = writeTemplate("Broken.clever", "@Page Broken\n@Code {\n@Template { <p>Broken</p> }");

		IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
				() -> compiler.compile(template, "com.example"));

		assertTrue(error.getMessage().contains("Broken.clever:"));
		assertTrue(error.getMessage().contains("Broken.clever:3:"));
	}

	@Test
	void compileRejectsExpressionsInAttributesWithSourceLocation() throws IOException {
		Path template = writeTemplate("Attribute.clever", """
				@Page AttributePage
				@Code { private String url = \"/\"; }
				@Template { <a href=\"@(url)\">link</a> }
				""");

		IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
				() -> compiler.compile(template, "com.example"));

		assertTrue(error.getMessage().contains("Attribute.clever:3:"));
		assertTrue(error.getMessage().contains("expressions in HTML attributes"));
	}

	@Test
	void compileRejectsInvalidJavaClassAndPackageNames() throws IOException {
		Path invalidClass = writeTemplate("Invalid.clever", "@Page class\n@Code {}\n@Template {}\n");
		Path validClass = writeTemplate("Valid.clever", "@Page Valid\n@Code {}\n@Template {}\n");

		assertTrue(assertThrows(IllegalArgumentException.class,
				() -> compiler.compile(invalidClass, "com.example")).getMessage().contains("invalid Java class name"));
		assertTrue(assertThrows(IllegalArgumentException.class,
				() -> compiler.compile(validClass, "com.class")).getMessage().contains("invalid Java package name"));
		assertDoesNotThrow(() -> compiler.compile(validClass, "com.example"));
	}

	@Test
	void compileAllowsDefaultPackageAndRejectsNonCleverFiles() throws IOException {
		Path validClass = writeTemplate("DefaultPackage.clever", "@Page DefaultPackage\n@Code {}\n@Template {}\n");
		Path otherFile = writeTemplate("Template.txt", "@Page Template\n@Code {}\n@Template {}\n");

		assertTrue(compiler.compile(validClass, null).startsWith("import com.projectclever"));
		assertTrue(assertThrows(IllegalArgumentException.class,
				() -> compiler.compile(otherFile, "")).getMessage().contains("expected a .clever template file"));
	}

	@Test
	void compileRejectsEmptyExpressions() throws IOException {
		Path template = writeTemplate("EmptyExpression.clever", "@Page Empty\n@Code {}\n@Template { <p>@()</p> }\n");

		IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
				() -> compiler.compile(template, ""));

		assertTrue(error.getMessage().contains("template expression cannot be empty"));
	}

	@Test
	void mainWritesGeneratedJavaToRequestedPath() throws IOException {
		Path template = writeTemplate("Hello.clever", "@Page Hello\n@Code {}\n@Template { <h1>Hello</h1> }\n");
		Path output = tempDir.resolve("generated/com/example/Hello.java");

		CleverCompiler.main(new String[] {template.toString(), "com.example", output.toString()});

		assertTrue(Files.readString(output).contains("public class Hello {"));
	}

	@Test
	void mainRejectsIncorrectArgumentCount() {
		IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
				() -> CleverCompiler.main(new String[0]));

		assertTrue(error.getMessage().contains("Usage: CleverCompiler"));
	}

	@Test
	void generatedRenderMethodReturnsEscapedHtml() throws Exception {
		Path template = writeTemplate("EscapedPage.clever", """
				@Page EscapedPage
				@Code { private String input = \"<script>alert(1)</script>\"; }
				@Template { <p>Hello @(input)</p> }
				""");
		Path sourceFile = tempDir.resolve("generated/com/example/EscapedPage.java");
		Path classes = tempDir.resolve("classes");
		Files.createDirectories(sourceFile.getParent());
		Files.createDirectories(classes);
		Files.writeString(sourceFile, compiler.compile(template, "com.example"));
		List<Class<?>> runtimeTypes = List.of(CleverCompiler.class, HtmlTagGenerator.class, Jsoup.class, Element.class,
				StringEscapeUtils.class, org.apache.commons.lang3.StringUtils.class);
		List<String> classpathEntries = new java.util.ArrayList<>();
		for (Class<?> type : runtimeTypes) {
			String entry = Path.of(type.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
			if (!classpathEntries.contains(entry)) {
				classpathEntries.add(entry);
			}
		}
		String classpath = String.join(System.getProperty("path.separator"), classpathEntries);
		int result = ToolProvider.getSystemJavaCompiler().run(null, null, null, "-classpath", classpath,
				"-d", classes.toString(), sourceFile.toString());

		assertEquals(0, result);
		try (URLClassLoader loader = new URLClassLoader(new URL[] {classes.toUri().toURL()}, getClass().getClassLoader())) {
			Object page = loader.loadClass("com.example.EscapedPage").getConstructor().newInstance();
			assertEquals(" <p>Hello &lt;script&gt;alert(1)&lt;/script&gt;</p> ",
					page.getClass().getMethod("render").invoke(page));
		}
	}

	private Path writeTemplate(String name, String source) throws IOException {
		Path template = tempDir.resolve(name);
		Files.writeString(template, source);
		return template;
	}
}
