## ADDED Requirements

### Requirement: Template source files
The compiler SHALL recognize `.clever` files as Clever template source files. Each template file SHALL declare exactly one template kind: Page, Component, or Layout, together with the generated class name.

#### Scenario: Declared page template
- **WHEN** a `.clever` file declares `@Page DashboardPage`
- **THEN** the compiler identifies it as a page template named `DashboardPage`

#### Scenario: Missing template declaration
- **WHEN** a `.clever` file does not contain a template kind and class name
- **THEN** the compiler reports a source diagnostic for the missing declaration

### Requirement: Code and template blocks
The language SHALL provide an `@Code { ... }` block for Java declarations and an `@Template { ... }` block for template content. Neither block SHALL take parenthesized arguments. The compiler SHALL preserve Java declarations as members of the generated class.

#### Scenario: Java class member
- **WHEN** a template declares a Java method in `@Code { ... }`
- **THEN** the generated class exposes the method as a class member

#### Scenario: Missing required block
- **WHEN** a template omits a required primary block
- **THEN** the compiler reports a diagnostic identifying the missing block in the source file

### Requirement: Typed template inputs
The language SHALL provide an `@Inputs(Type name, ...)` directive to declare strongly typed values supplied when a component or layout is invoked. The compiler SHALL generate corresponding constructor parameters and fields and make the values available in the template's Java code and markup.

`@Inputs(...)` SHALL contain at least one parameter and SHALL be declared only by a Component or Layout. Generated input fields SHALL be private and final. The compiler SHALL reject duplicate or invalid parameter names and SHALL validate component and layout argument counts. Java compilation SHALL validate argument types.

#### Scenario: Inputs become constructor values
- **WHEN** a component declares `@Inputs(String name, int age)`
- **THEN** its generated class stores those typed values and exposes a constructor accepting them in declaration order

#### Scenario: Input is available in template
- **WHEN** a template declares an input and references it in `@Code` or `@Template`
- **THEN** the generated class resolves the reference to the declared input value

#### Scenario: Invalid input declaration
- **WHEN** an input declaration contains invalid Java types or duplicate parameter names
- **THEN** the compiler reports a source diagnostic for the declaration

### Requirement: Java class generation
The compiler SHALL generate a Java class whose name matches the template's declared class name. Generated classes SHALL contain rendering code for the template content and SHALL be compiled through the project's Java build.

#### Scenario: Build compiles a valid template
- **WHEN** a valid `.clever` template is passed to the `compileCleverJava` Gradle task with an output package and source path
- **THEN** the build generates and compiles its corresponding Java class

### Requirement: Source diagnostics
The compiler SHALL report template syntax and validation errors against the original `.clever` file with source locations when available.

#### Scenario: Invalid template syntax
- **WHEN** the parser encounters invalid template syntax
- **THEN** the diagnostic identifies the source file and location of the error

The initial compiler SHALL accept Java expressions in template text using `@(JavaExpression)` and SHALL report source locations for malformed template syntax.
