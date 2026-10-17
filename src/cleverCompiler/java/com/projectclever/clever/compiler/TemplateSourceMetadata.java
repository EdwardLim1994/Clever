package com.projectclever.clever.compiler;

import java.util.List;
import java.util.Set;

record TemplateSourceMetadata(List<String> inputs, Set<String> publicFields, Set<String> layoutSlots) {
}
