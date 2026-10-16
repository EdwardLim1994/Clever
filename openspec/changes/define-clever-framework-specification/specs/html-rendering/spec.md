## ADDED Requirements

### Requirement: Compile-time HTML rendering
The runtime SHALL render templates using Java classes generated at build time rather than parsing template files on each HTTP request.

#### Scenario: Render compiled template
- **WHEN** the application renders a compiled template
- **THEN** generated Java rendering code writes the template output

### Requirement: Compiled template rendering facade
The runtime SHALL provide a concise Java API that renders a compiled template class using named constructor inputs.

#### Scenario: Render a compiled template with named inputs
- **WHEN** an application calls `CleverRenderer.render(HelloPage.class, Map.of("name", name))`
- **THEN** the runtime creates the compiled template using matching constructor parameters and returns its rendered HTML
- **AND** the input map's iteration order does not affect constructor argument binding

#### Scenario: Resolve a template class by name
- **WHEN** an application renders a compiled template whose class is not available at Java compile time
- **THEN** the runtime loads the named compiled class and renders it using the same named-input rules

### Requirement: Dynamic text escaping
The renderer SHALL HTML-escape dynamic values emitted as text by default.

#### Scenario: Render dynamic text
- **WHEN** a template emits a dynamic value containing HTML-significant characters as text
- **THEN** the output contains escaped text and does not interpret the value as markup

### Requirement: Template expressions
The template language SHALL allow Java expressions in HTML text nodes using `@(JavaExpression)`. Attribute expressions and raw HTML expressions are not supported in the initial compiler slice.

#### Scenario: Evaluate expression in template
- **WHEN** a template contains a valid dynamic Java expression
- **THEN** the generated renderer evaluates the expression and emits its value using the default escaping rules
