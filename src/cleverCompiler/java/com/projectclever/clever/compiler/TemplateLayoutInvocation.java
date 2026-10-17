package com.projectclever.clever.compiler;

import java.util.Map;

record TemplateLayoutInvocation(String constructor, Map<String, TemplateBlock> slots) {
}
