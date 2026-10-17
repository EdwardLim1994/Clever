package com.projectclever.clever;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

@Component
public final class HtmlHttpMessageConverter extends AbstractHttpMessageConverter<CleverHtml> {
	public HtmlHttpMessageConverter() {
		super(StandardCharsets.UTF_8, MediaType.TEXT_HTML);
	}

	@Override
	protected boolean supports(Class<?> type) {
		return CleverHtml.class.isAssignableFrom(type);
	}

	@Override
	protected CleverHtml readInternal(Class<? extends CleverHtml> type, HttpInputMessage input) throws HttpMessageNotReadableException {
		throw new HttpMessageNotReadableException("HTML responses cannot be read", input);
	}

	@Override
	protected void writeInternal(CleverHtml html, HttpOutputMessage output) throws IOException {
		output.getBody().write(html.value().getBytes(StandardCharsets.UTF_8));
	}
}
