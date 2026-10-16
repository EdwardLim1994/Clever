package com.projectclever.clever;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

/** Entry point for rendering compiled Clever templates. */
public final class CleverRenderer {

	private CleverRenderer() {
	}

	/** Renders a template created with its Java constructor. */
	public static CleverHtml render(Object template) {
		if (template == null) {
			throw new IllegalArgumentException("Template is required");
		}
		Class<?> templateType = template.getClass();
		try {
			Method render = templateType.getMethod("render");
			if (render.getParameterCount() != 0 || render.getReturnType() != String.class) {
				throw new IllegalArgumentException(templateType.getName() + ".render() must return String");
			}
			return new CleverHtml((String) render.invoke(template));
		} catch (InvocationTargetException exception) {
			throw new IllegalStateException("Could not render " + templateType.getName(), exception.getCause());
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException("Could not render " + templateType.getName(), exception);
		}
	}

	/**
	 * Creates a compiled template using named constructor inputs and returns its
	 * rendered HTML.
	 */
	public static CleverHtml render(Class<?> templateType, Map<String, ?> inputs) {
		if (templateType == null || inputs == null) {
			throw new IllegalArgumentException("Template type and inputs are required");
		}

		Constructor<?> constructor = Arrays.stream(templateType.getConstructors())
				.filter(candidate -> matchesInputs(candidate, inputs))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(
						"No public constructor on " + templateType.getName() + " matches input names and values"));
		try {
			Object[] arguments = Arrays.stream(constructor.getParameters())
					.map(Parameter::getName)
					.map(inputs::get)
					.toArray();
			return render(constructor.newInstance(arguments));
		} catch (InvocationTargetException exception) {
			throw new IllegalStateException("Could not render " + templateType.getName(), exception.getCause());
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException("Could not render " + templateType.getName(), exception);
		}
	}

	/** Loads and renders a compiled template by fully qualified class name. */
	public static CleverHtml render(String templateClassName, Map<String, ?> inputs) {
		if (templateClassName == null || templateClassName.isBlank()) {
			throw new IllegalArgumentException("Template class name is required");
		}
		try {
			return render(Class.forName(templateClassName), inputs);
		} catch (ClassNotFoundException exception) {
			throw new IllegalArgumentException("Compiled template class not found: " + templateClassName, exception);
		}
	}

	private static boolean matchesInputs(Constructor<?> constructor, Map<String, ?> inputs) {
		Parameter[] parameters = constructor.getParameters();
		if (parameters.length != inputs.size()
				|| Arrays.stream(parameters).anyMatch(parameter -> !parameter.isNamePresent())) {
			return false;
		}
		return Arrays.stream(parameters).allMatch(parameter -> {
			if (!inputs.containsKey(parameter.getName())) {
				return false;
			}
			Object value = inputs.get(parameter.getName());
			return value == null ? !parameter.getType().isPrimitive() : boxed(parameter.getType()).isInstance(value);
		});
	}

	private static Class<?> boxed(Class<?> type) {
		return switch (type) {
			case Class<?> primitive when primitive == boolean.class -> Boolean.class;
			case Class<?> primitive when primitive == byte.class -> Byte.class;
			case Class<?> primitive when primitive == short.class -> Short.class;
			case Class<?> primitive when primitive == int.class -> Integer.class;
			case Class<?> primitive when primitive == long.class -> Long.class;
			case Class<?> primitive when primitive == char.class -> Character.class;
			case Class<?> primitive when primitive == float.class -> Float.class;
			case Class<?> primitive when primitive == double.class -> Double.class;
			case Class<?> primitive when primitive == void.class -> Void.class;
			default -> type;
		};
	}
}
