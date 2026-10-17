package com.projectclever.clever;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.Test;

class CleverRendererTests {

	@Test
	void renderAcceptsConstructedTemplate() {
		assertEquals(new CleverHtml("Julian Bashir (29)"), CleverRenderer.render(new TemplateFixture("Julian Bashir", 29)));
	}

	@Test
	void renderRejectsNullTemplate() {
		assertThrows(IllegalArgumentException.class, () -> CleverRenderer.render((Object) null));
	}

	@Test
	void renderBindsNamedInputsAndReturnsCompiledTemplateOutput() {
		assertEquals(new CleverHtml("Julian Bashir (29)"), CleverRenderer.render(TemplateFixture.class,
				Map.of("age", 29, "name", "Julian Bashir")));
	}

	@Test
	void renderByClassNameLoadsCompiledTemplate() {
		assertEquals(new CleverHtml("Julian Bashir (29)"), CleverRenderer.render(TemplateFixture.class.getName(),
				Map.of("name", "Julian Bashir", "age", 29)));
	}

	@Test
	void renderRejectsInputsThatDoNotMatchConstructor() {
		Map<String, String> inputs = Map.of("wrong", "name");
		assertThrows(IllegalArgumentException.class,
				() -> CleverRenderer.render(TemplateFixture.class, inputs));
	}

	@Test
	void renderRejectsMissingTemplateOrInputs() {
		Map<String, Object> emptyInputs = Map.of();
		assertThrows(IllegalArgumentException.class, () -> CleverRenderer.render((Class<?>) null, emptyInputs));
		assertThrows(IllegalArgumentException.class, () -> CleverRenderer.render(TemplateFixture.class, null));
		assertThrows(IllegalArgumentException.class, () -> CleverRenderer.render("", emptyInputs));
	}

	@Test
	void renderRejectsInvalidRenderMethods() {
		Object missing = new Object();
		WrongReturnFixture wrongReturn = new WrongReturnFixture();
		assertThrows(IllegalStateException.class, () -> CleverRenderer.render(missing));
		assertThrows(IllegalArgumentException.class, () -> CleverRenderer.render(wrongReturn));
	}

	@Test
	void renderPreservesInvocationCauses() {
		ThrowingFixture throwing = new ThrowingFixture();
		Map<String, String> inputs = Map.of("name", "Ada");
		assertEquals("render failed", assertThrows(IllegalStateException.class,
				() -> CleverRenderer.render(throwing)).getCause().getMessage());
		assertEquals("construction failed", assertThrows(IllegalStateException.class,
				() -> CleverRenderer.render(ConstructorFailureFixture.class, inputs)).getCause().getMessage());
	}

	@Test
	void renderByClassNameReportsMissingTemplate() {
		assertThrows(IllegalArgumentException.class,
				() -> CleverRenderer.render("missing.Template", Map.of()));
	}

	public static class TemplateFixture {
		private final String name;
		private final int age;

		public TemplateFixture(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String render() {
			return name + " (" + age + ")";
		}
	}

	public static class WrongReturnFixture {
		public int render() {
			return 1;
		}
	}

	public static class ThrowingFixture {
		public String render() {
			throw new IllegalArgumentException("render failed");
		}
	}

	public static class ConstructorFailureFixture {
		public ConstructorFailureFixture(String name) {
			throw new IllegalArgumentException("construction failed");
		}
	}
}
