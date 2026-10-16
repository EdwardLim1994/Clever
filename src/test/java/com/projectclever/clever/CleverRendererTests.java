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
		assertThrows(IllegalArgumentException.class,
				() -> CleverRenderer.render(TemplateFixture.class, Map.of("wrong", "name")));
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
}
