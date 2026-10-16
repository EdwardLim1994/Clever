package com.projectclever.clever.compiler;

import java.util.Map;
import lombok.Builder;
import org.jsoup.nodes.Element;

@Builder
record TemplateBlock(Element body, Map<String, TemplateExpression> expressions) {
}
