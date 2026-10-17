package com.projectclever.clever;

import java.util.Objects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/** Rendered HTML ready to be written as an HTTP response. */
public record CleverHtml(String value) {
	private static final String VALUE_ATTRIBUTE = "value";
	private static final Safelist SAFE_HTML = Safelist.relaxed()
			.addTags("main", "section", "article", "header", "footer", "nav", "aside", "button", "form", "input", "label")
			.addAttributes(":all", "class", "id", "title", "role")
			.addAttributes("form", "action", "method")
			.addAttributes("input", "type", "name", VALUE_ATTRIBUTE, "placeholder")
			.addAttributes("button", "type", "name", VALUE_ATTRIBUTE)
			.addProtocols("form", "action", "http", "https")
			.preserveRelativeLinks(true);

	public CleverHtml {
		Objects.requireNonNull(value, VALUE_ATTRIBUTE);
		value = Jsoup.clean(value, "https://clever.invalid/", SAFE_HTML, new Document.OutputSettings().prettyPrint(false));
	}
}
