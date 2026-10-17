package com.projectclever.clever.compiler;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

final class ComponentTemplateMetadata {

	private ComponentTemplateMetadata() {
	}

	static Set<String> publicFields(String code) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw new IllegalStateException("a JDK compiler is required to inspect component fields");
		}
		String wrappedCode = "class CleverComponentMetadata {\n" + code + "\n}";
		JavaFileObject source = new SimpleJavaFileObject(URI.create("string:///CleverComponentMetadata.java"),
				JavaFileObject.Kind.SOURCE) {
			@Override
			public CharSequence getCharContent(boolean ignoreEncodingErrors) {
				return wrappedCode;
			}
		};
		JavacTask task = (JavacTask) compiler.getTask(null, null, new DiagnosticCollector<>(), List.of("-proc:none"),
				null, List.of(source));
		try {
			CompilationUnitTree unit = task.parse().iterator().next();
			ClassTree type = (ClassTree) unit.getTypeDecls().getFirst();
			Set<String> fields = new HashSet<>();
			for (Tree member : type.getMembers()) {
				if (member instanceof VariableTree field && field.getModifiers().getFlags().contains(javax.lang.model.element.Modifier.PUBLIC)) {
					fields.add(field.getName().toString());
				}
			}
			return Set.copyOf(fields);
		} catch (java.io.IOException exception) {
			throw new IllegalArgumentException("could not parse component Java members", exception);
		}
	}
}
