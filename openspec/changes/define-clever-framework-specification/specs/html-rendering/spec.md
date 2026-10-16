## ADDED Requirements

### Requirement: Compile-time HTML rendering
The runtime SHALL render templates using Java classes generated at build time rather than parsing template files on each HTTP request.

#### Scenario: Render compiled template
- **WHEN** the application renders a compiled template
- **THEN** generated Java rendering code writes the template output

### Requirement: Dynamic text escaping
The renderer SHALL HTML-escape dynamic values emitted as text by default.

#### Scenario: Render dynamic text
- **WHEN** a template emits a dynamic value containing HTML-significant characters as text
- **THEN** the output contains escaped text and does not interpret the value as markup

### Requirement: Template expressions
The template language SHALL allow Java expressions to provide dynamic values in HTML content. Exact expression delimiters and supported expression contexts remain language-design decisions.

#### Scenario: Evaluate expression in template
- **WHEN** a template contains a valid dynamic Java expression
- **THEN** the generated renderer evaluates the expression and emits its value using the default escaping rules
