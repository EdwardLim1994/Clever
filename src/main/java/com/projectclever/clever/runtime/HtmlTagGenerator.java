package com.projectclever.clever.runtime;

import java.util.Map;
import java.util.Objects;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public final class HtmlTagGenerator {

	private HtmlTagGenerator() {
	}

	public static Element element(String tagName, Map<String, ?> attributes) {
		Element element = new Element(Tag.valueOf(tagName), "");
		attributes.forEach((name, value) -> {
			if (value != null) {
				element.attr(name, value.toString());
			}
		});
		return element;
	}

	public static void appendText(Element element, Object value) {
		element.appendText(Objects.toString(value, ""));
	}

	public static String render(Element element) {
		return element.outerHtml();
	}
}
