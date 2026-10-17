package com.projectclever.clever.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.Test;

class ComponentTemplateMetadataTests {

	@Test
	void publicFieldsReturnsOnlyPublicFieldsDeclaredOnComponent() {
		String code = "public int count = 1; private String secret; protected String hidden;";

		assertEquals(Set.of("count"), ComponentTemplateMetadata.publicFields(code));
	}
}
