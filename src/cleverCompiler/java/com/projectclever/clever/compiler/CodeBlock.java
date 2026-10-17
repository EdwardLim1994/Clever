package com.projectclever.clever.compiler;

import java.util.List;
import lombok.Builder;

@Builder
record CodeBlock(String source, List<String> imports) {
}
