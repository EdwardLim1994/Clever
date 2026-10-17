package com.projectclever.clever;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;

class HtmlTests {
	@Test
	void htmlRequiresContent() {
		assertThrows(NullPointerException.class, () -> new CleverHtml(null));
		assertEquals("<p>Hello</p>", new CleverHtml("<p>Hello</p>").value());
	}

	@Test
	void htmlRemovesScriptsEventHandlersAndUnsafeUrls() {
		String html = new CleverHtml("<main><a href=\"javascript:alert(1)\" onclick=\"alert(1)\">Go</a>"
				+ "<script>alert(1)</script><p>Safe &amp; sound</p></main>").value();

		assertEquals("<main><a>Go</a><p>Safe &amp; sound</p></main>", html);
	}

	@Test
	void htmlKeepsSafeTemplateMarkupAndRelativeLinks() {
		assertEquals("<main class=\"page\"><a href=\"/home\">Home</a></main>",
				new CleverHtml("<main class=\"page\"><a href=\"/home\">Home</a></main>").value());
	}

	@Test
	void converterWritesHtmlWithHtmlContentType() throws IOException {
		HtmlHttpMessageConverter converter = new HtmlHttpMessageConverter();
		MockHttpOutputMessage output = new MockHttpOutputMessage();

		converter.write(new CleverHtml("<p>Hello</p>"), null, output);

		assertEquals("<p>Hello</p>", output.getBodyAsString());
		assertTrue(MediaType.TEXT_HTML.isCompatibleWith(output.getHeaders().getContentType()));
	}

	@Test
	void converterRejectsReadingHtml() {
		HtmlHttpMessageConverter converter = new HtmlHttpMessageConverter();
		MockHttpInputMessage input = new MockHttpInputMessage(new byte[0]);

		assertThrows(HttpMessageNotReadableException.class,
				() -> converter.readInternal(CleverHtml.class, input));
	}
}
