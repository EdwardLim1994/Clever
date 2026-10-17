package com.projectclever.clever.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

class HtmlTagGeneratorTests {

	@Test
	void elementCreatesTagWithEscapedAttributes() {
		Element element = HtmlTagGenerator.element("a", Map.of("href", "/search?q=a&sort=asc"));

		assertEquals("<a href=\"/search?q=a&amp;sort=asc\"></a>", HtmlTagGenerator.render(element));
	}

	@Test
	void elementSkipsNullAttributeValues() {
		Element element = HtmlTagGenerator.element("a", java.util.Collections.singletonMap("href", null));

		assertEquals("<a></a>", HtmlTagGenerator.render(element));
	}

	@Test
	void appendTextEscapesHtmlContent() {
		Element element = HtmlTagGenerator.element("div", Map.of());

		HtmlTagGenerator.appendText(element, "<script>alert('x')</script> &");

		assertEquals("<div>&lt;script&gt;alert('x')&lt;/script&gt; &amp;</div>", HtmlTagGenerator.render(element));
	}

	@Test
	void appendTextEscapesExistingEntitiesWithoutReparsing() {
		Element element = HtmlTagGenerator.element("p", Map.of());
		HtmlTagGenerator.appendText(element, "&lt;script&gt; & \"hi\"");
		assertEquals("<p>&amp;lt;script&amp;gt; &amp; \"hi\"</p>", HtmlTagGenerator.render(element));
	}

	@Test
	void appendTextTreatsNullAsEmpty() {
		Element element = HtmlTagGenerator.element("span", Map.of());

		HtmlTagGenerator.appendText(element, null);

		assertEquals("<span></span>", HtmlTagGenerator.render(element));
	}

	@Test
	void renderSerializesNestedGeneratedElements() {
		Element parent = HtmlTagGenerator.element("section", Map.of());
		Element child = HtmlTagGenerator.element("p", Map.of());
		HtmlTagGenerator.appendText(child, "Hello");
		parent.appendChild(child);

		assertEquals("<section>\n <p>Hello</p>\n</section>", HtmlTagGenerator.render(parent));
	}
}
