# Project Summary: Java-Native Frontend Framework for Spring Boot

## 1. Project Overview

The goal is to develop a new Java-native frontend framework specifically designed for Spring Boot applications.

The framework will allow developers to write native Java code and HTML templates within a single custom template file. During compilation, the custom template files will be transformed into Java classes that generate HTML responses.

The framework aims to provide a modern frontend development experience without requiring a separate JavaScript frontend framework such as React, Vue, or Angular.

The primary motivation is to address the perceived gap in the Java ecosystem between traditional server-side template engines and modern component-based frontend frameworks.

### Core Inspirations

1. **Microsoft Blazor / Razor**
   - Write native programming language code alongside HTML.
   - Class-oriented component development.
   - Reusable components.
   - Integrated frontend and backend development.

2. **Laravel Blade**
   - Simple, expressive template directives.
   - Layout inheritance and composition.
   - Named slots.
   - Reusable components.
   - Minimal frontend configuration.

3. **HTMX**
   - Server-driven frontend interactivity.
   - AJAX requests triggered through HTML attributes or directives.
   - Partial HTML updates without full page reloads.
   - Avoid requiring developers to write JavaScript for common interactive behavior.

4. **Spring Boot**
   - Native Java integration.
   - Dependency injection.
   - Service integration.
   - HTTP request handling.
   - Spring Security compatibility.

The intended outcome is a Java-first, HTML-native, server-driven UI framework.

---

## 2. Fundamental Architecture

The framework will use a compile-time approach.

Development pipeline:

Custom Template File
→ Template Parser
→ Abstract Syntax Tree (AST)
→ Java Source Generator
→ Generated Java Class
→ Java Compiler
→ JVM Execution
→ HTML Response

Custom files will contain both Java code and HTML templates.

The compiler must understand the distinction between Java syntax and HTML/template syntax.

### Build-Time Responsibilities

- Parse custom template files.
- Identify template types.
- Parse native Java declarations.
- Parse HTML structures.
- Resolve directives.
- Resolve component and layout references.
- Validate parameters and variable types.
- Validate variable visibility and scope.
- Generate Java source code.
- Report compilation errors against the original template files.

### Runtime Responsibilities

- Instantiate generated Java template classes.
- Integrate with Spring Boot.
- Render HTML.
- Execute server-side actions.
- Handle AJAX requests.
- Generate updated HTML fragments.
- Return responses to the browser.
- Support partial DOM updates through a lightweight browser runtime.

The framework should minimize runtime interpretation because templates are compiled into Java classes.

---

## 3. Custom Template File Structure

Every template file will contain a declaration identifying its type.

Three initial template types have been identified:

- Page
- Component
- Layout

Examples:

```java
@Page DashboardPage
```

```java
@Component UserCard
```

```java
@Layout MainLayout
```

The declared class name will determine the name of the generated Java class.

For example:

```java
@Page DashboardPage

@Code {
    // Native Java code
}

@Template {
    <h1>Dashboard</h1>
}
```

Will generate a Java class named `DashboardPage`.

Each template contains two primary blocks.

### @Code {...}

Contains native Java declarations, including:

- Import statements.
- Fields.
- Methods.
- Access modifiers.
- Java annotations.
- Java business logic.
- Dependencies.

The code block is class-oriented rather than render-oriented.

This means Java fields and methods declared inside the block become members of the generated Java class.

Import statements will be placed at the appropriate location in the generated Java compilation unit.

The goal is to preserve native Java syntax and behavior as much as possible.

### @Template {...}

Contains:

- HTML elements.
- Java expressions.
- Template directives.
- Conditional rendering.
- Iteration.
- Component invocation.
- Layout invocation.
- Slot declarations and content.
- Future AJAX event bindings.

The HTML template will be compiled into Java rendering statements.

---

## 4. Template Inputs

The `@Inputs(...)` directive declares strongly typed values supplied when a component or layout is invoked. The compiler generates constructor parameters and fields for those inputs. `@Code { ... }` remains a Java member block, and `@Template { ... }` contains rendered template content.

Example:

```java
@Component UserCard

@Inputs(String name, int age)

@Code {
    private String formatName() {
        return name.toUpperCase();
    }
}

@Template {
    <div>
        <h2>@formatName()</h2>
        <p>Age: @age</p>
    </div>
}
```

The inputs `name` and `age` are accessible inside both the code block and template block.

Conceptually, the compiler could generate:

```java
public class UserCard {

    private final String name;
    private final int age;

    public UserCard(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private String formatName() {
        return name.toUpperCase();
    }

    public void render(HtmlWriter writer) {
        // Generated HTML rendering code
    }
}
```

### Input Rules

- Inputs use native Java type declarations and retain their Java types.
- Inputs are available inside the generated class and template.
- Inputs can be passed to nested components.
- Input order determines generated constructor argument order.
- The framework compiler validates argument types.

The current preferred approach is to generate private final fields and a constructor for declared inputs. Custom Java constructor behavior can be declared in `@Code` when needed.

Default parameter values have not been decided.

---

## 5. Component System

Components are reusable HTML fragments backed by generated Java classes.

A component is declared using:

```java
@Component ComponentName
```

A component can be invoked within another template using:

```java
@Component(ComponentName.class)
```

Parameters can be provided as additional arguments:

```java
@Component(ComponentName.class, argument1, argument2)
```

Example component:

```java
@Component UserCard

@Inputs(String name, int age)

@Code {
    private String getDisplayName() {
        return name.toUpperCase();
    }
}

@Template {
    <article>
        <h2>@getDisplayName()</h2>
        <p>Age: @age</p>
    </article>
}
```

Example page:

```java
@Page DashboardPage

@Code {
    private String username = "John";
    private int age = 30;
}

@Template {
    <h1>Dashboard</h1>

    @Component(UserCard.class, username, age)
}
```

The compiler resolves the component class and generates the necessary Java rendering calls.

Component references should be type-safe and validated during compilation.

---

## 6. Layout System

Layouts provide reusable page structures.

A layout is declared with:

```java
@Layout MainLayout
```

Layouts support named slots.

Example:

```java
@Layout MainLayout

@Inputs(String title)

@Code {
}

@Template {
    <!DOCTYPE html>
    <html>
        <head>
            <title>@title</title>
        </head>

        <body>
            <header>
                @Slot("header")
            </header>

            <main>
                @Slot("content")
            </main>

            <footer>
                @Slot("footer")
            </footer>
        </body>
    </html>
}
```

### Layout Invocation

The agreed direction is to invoke layouts through:

```java
@Layout(MainLayout.class, "Dashboard"){
    @Slot("header"){
        <h1>Dashboard</h1>
    }

    @Slot("content"){
        <p>Welcome to the application.</p>
    }

    @Slot("footer"){
        <p>Copyright 2026</p>
    }
}
```

The first argument identifies the generated Java layout class.

Additional arguments supply values declared through `@Inputs(...)`.

### Slot System

A slot has two roles.

**Slot declaration in a layout:**

```java
@Slot("content")
```

Defines an insertion point.

**Slot assignment inside layout invocation:**

```java
@Slot("content"){
    <h1>Page Content</h1>
}
```

Provides content for the named insertion point.

### Proposed Slot Rules

- A layout may contain multiple named slots.
- A page can supply different HTML fragments to different slots.
- A slot's content may contain Java expressions, HTML, and components.
- Slot names should be validated during compilation.
- Duplicate slot assignments should produce compilation errors unless explicitly supported.
- Layouts may eventually support default slot content.
- Layout nesting should be supported.

Example nested layout:

```java
@Layout AdminLayout

@Code {
    import com.example.ui.MainLayout;
}

@Template {
    @Layout(MainLayout.class){
        @Slot("content"){
            <div class="admin">
                @Slot("adminContent")
            </div>
        }
    }
}
```

This allows layout composition without requiring a separate inheritance mechanism.

---

## 7. Variable Visibility and Scope

An important design decision is that Java's `private` and `public` modifiers will influence visibility within the template composition system.

### Private Variables

A private variable belongs to the template class that declares it.

Example:

```java
@Code {
    private String internalMessage = "Internal";
}
```

The variable can be accessed within the same template's Java code and HTML rendering expressions.

It cannot be automatically accessed by a parent template.

### Public Variables

A public variable can be automatically exposed to the immediate parent template that invokes the component.

Example:

Counter component:

```java
@Component Counter

@Inputs(int initialCount)

@Code {
    public int count = initialCount;
    private int step = 1;
}

@Template {
    <button>Count: @count</button>
}
```

Parent page:

```java
@Page DashboardPage

@Code {
}

@Template {
    @Component(Counter.class, 10)

    <p>Counter value: @count</p>
}
```

Under the proposed framework semantics, the public `count` field is automatically accessible from the parent template following the component invocation.

The private `step` field is not exposed.

### Important Design Intention

The framework should not require an additional `@Ref` annotation merely to access public component variables.

The developer's decision to declare a variable `public` is intended to make that variable available to the invoking parent.

This is a deliberate extension of standard Java visibility, implemented through the template compiler.

### Proposed Scope Rules

1. Private fields are accessible only within the declaring template class, subject to normal Java rules.
2. Public fields are eligible for automatic exposure to the immediate parent template.
3. Exposed fields retain their Java types.
4. Child component inputs flow from parent to child through `@Inputs(...)` declarations and component invocation arguments.
5. Public component exports flow from child to parent.
6. Public exports become accessible after the component invocation within template rendering order.
7. Exporting multiple variables with the same name into one scope should produce a compiler error.
8. Public variables should not automatically propagate across every ancestor level.
9. Public exports do not automatically imply reactive synchronization across HTTP requests.

The approach to automatically exporting public methods has not been finalized. Initial emphasis is on public fields.

### Example of Name Collision

```java
@Template {
    @Component(Counter.class, 10)
    @Component(Counter.class, 20)

    <p>@count</p>
}
```

Both components export `count`.

The recommended behavior is to report an ambiguous variable error during compilation instead of silently selecting one component's value.

This area requires a formal scope-resolution specification.

---

## 8. Template Expression Syntax

The framework should support Java expressions directly within HTML.

The initial compiler supports `@(JavaExpression)` in HTML text nodes. Attribute expressions and raw HTML expressions are not supported in this slice; the other examples below describe future language direction.

Illustrative examples:

```html
<h1>@username</h1>

<p>@user.getName()</p>

<p>@(price.multiply(taxRate))</p>
```

Basic control-flow directives might include:

```java
@if (user.isActive()) {
    <span>Active</span>
} else {
    <span>Inactive</span>
}
```

And:

```java
@foreach (User user : users) {
    <UserCard />
}
```

The exact directive grammar remains open for discussion.

The guiding principle is to make expressions as close to native Java as possible.

The compiler must handle nested Java expressions, parentheses, blocks, string literals, and HTML markup correctly.

Regular-expression-only parsing is not recommended.

---

## 9. AJAX and Hybrid Rendering

The framework is intended to support hybrid frontend rendering.

Initial pages are rendered by Spring Boot.

Browser interactions can trigger AJAX requests to server-side Java methods.

The server processes the request, renders an updated HTML fragment, and returns it to the browser.

A lightweight JavaScript runtime updates the appropriate DOM elements.

### Intended Interaction Flow

Browser Event
→ AJAX Request
→ Spring Boot
→ Java Action Method
→ Component Rendering
→ HTML Fragment Response
→ Browser DOM Update

The framework should make this process declarative.

Illustrative syntax:

```java
@Component Counter

@Code {
    private int count = 0;

    @Action
    public void increment() {
        count++;
    }
}

@Template {
    <div id="counter">
        <h1>@count</h1>

        <button
            @click="increment()"
            @target="#counter">
            Increment
        </button>
    </div>
}
```

The exact `@Action`, `@click`, and `@target` conventions have not been finalized.

They represent the desired direction rather than an approved language specification.

### Browser Runtime Responsibilities

- Handle user events.
- Submit AJAX requests.
- Serialize forms and parameters.
- Receive HTML fragments.
- Replace or morph DOM elements.
- Handle errors and loading states.
- Preserve input and focus state when appropriate.
- Support progressive enhancement where possible.

A full JavaScript virtual DOM is not required for the initial framework.

HTMX can serve as an initial implementation option or inspiration for the browser interaction layer.

---

## 10. Java Class Generation

Every template will become a Java class.

The generated class should contain:

- The name declared by `@Page`, `@Component`, or `@Layout`.
- Java imports extracted from `@Code`.
- Inputs represented as generated constructor parameters and fields.
- Java fields and methods declared in `@Code`.
- Generated HTML rendering methods.
- Metadata needed for component composition and interaction.

HTML rendering should ideally use an output abstraction such as `HtmlWriter`.

Example:

```java
public void render(HtmlWriter writer) {
    writer.write("<h1>");
    writer.escape(title);
    writer.write("</h1>");
}
```

This is preferable to parsing templates during each HTTP request.

Automatic escaping should be used for dynamic text to reduce HTML injection risks.

The compilation approach should provide:

- Strong Java typing.
- Compile-time validation.
- Efficient runtime rendering.
- Java ecosystem compatibility.
- Better error reporting.
- Opportunities for JVM optimization.

---

## 11. Spring Boot Integration

The framework should integrate directly with Spring Boot.

The framework runtime can provide:

- Component instantiation.
- Dependency injection.
- Page rendering.
- Controller integration.
- HTML response generation.
- Action dispatch.
- Spring Security integration.
- Request parameter handling.

Components should not automatically behave as singleton Spring beans, particularly when they contain mutable instance fields.

A suggested starting lifecycle is to create fresh component instances for rendering and actions, with persistent application state managed explicitly.

An AJAX request should not assume that the same Java component instance remains alive between requests.

Server-side services, repositories, sessions, or explicit state stores can provide persistent state where required.

---

## 12. Suggested Internal Modules

Proposed framework structure:

```text
framework/
│
├── language/
│   ├── lexer/
│   ├── parser/
│   ├── ast/
│   └── diagnostics/
│
├── compiler/
│   ├── semantic/
│   ├── scope/
│   ├── component/
│   ├── layout/
│   └── codegen/
│
├── runtime/
│   ├── rendering/
│   ├── component/
│   ├── layout/
│   ├── actions/
│   └── context/
│
├── spring-boot-starter/
│   ├── autoconfigure/
│   ├── mvc/
│   └── security/
│
├── browser-runtime/
│   └── runtime.js
│
├── maven-plugin/
├── gradle-plugin/
│
└── examples/
```

The compiler should ideally remain independent of Spring Boot.

Spring Boot integration should be provided through a separate adapter or starter module.

---

## 13. Confirmed Design Directions

The following reflect the user's expressed preferences during the discussions:

1. Build a Java-native HTML frontend framework intended for Spring Boot.
2. Compile custom templates into Java classes.
3. Support HTML-over-the-wire hybrid rendering with AJAX-based partial updates.
4. Use Razor and Blade as major syntax and developer-experience inspirations.
5. Use `@Code { ... }` and `@Template { ... }` as the primary file blocks.
6. Keep the Java code block class-oriented and close to native Java.
7. Require each template file to declare whether it is a Page, Component, or Layout.
8. Use the declared template class name as the generated Java class name.
9. Invoke components using `@Component(JavaClass.class, ...)`.
10. Invoke layouts using `@Layout(JavaClass.class, ...){...}`.
11. Support multiple named layout slots through `@Slot("name")`.
12. Use `@Inputs(Type name, ...)` to declare strongly typed component and layout inputs; generate constructor parameters and fields from them.
13. Allow private fields for internal template implementation.
14. Automatically expose public component fields to the parent template.
15. Avoid adding a separate component-reference annotation merely to access exported public fields.
16. Use `.clever` as the template file extension.

---

## 14. Recommendations That Are Not Yet Finalized

Several suggestions were discussed but should not be treated as approved framework requirements.

These include:

- The exact HTML expression syntax.
- The exact Java control-flow directive syntax.
- Whether public methods should also be automatically exported.
- Whether public exports are available only after component invocation.
- Whether child exports propagate through nested layouts.
- Whether slots are required or optional.
- Whether slots support fallback content.
- Whether `@Inputs(...)` supports defaults or optional values.
- Constructor and dependency-injection behavior.
- How Java package declarations should work.
- How pages bind URL parameters.
- How component actions are registered.
- Whether HTMX is used directly or replaced with a custom runtime.
- Whether template state is persisted between HTTP requests.
- Handling conditional component invocations and their public exports.
- How duplicate public variable names are resolved.
- Whether layouts can export their public variables to invoking pages.

These need further design before implementing the complete compiler.

---

## 15. Important Technical Challenges

### Java and HTML Parsing

The compiler must correctly recognize Java and HTML syntax within a single custom file.

Nested braces, method bodies, expressions, comments, and strings must be parsed safely.

### Scope and Visibility

Automatically exposing public component fields is convenient but introduces nonstandard Java scoping behavior.

The compiler must define precisely when a public field is accessible, how multiple component instances are handled, and how ambiguous names are detected.

### Component Lifecycle

The framework needs a consistent object instantiation model that supports Spring dependency injection without accidentally sharing mutable fields across HTTP requests or users.

### AJAX State Management

Public Java variables do not automatically remain synchronized between independent server requests.

The runtime will need explicit state and re-rendering behavior.

### Compile-Time Validation

The compiler should detect invalid class references, parameters, missing slots, ambiguous variables, and invalid Java expressions wherever possible.

### Security

HTML expressions must be properly escaped.

Server-side actions need authorization, CSRF protection, and input validation.

Only explicitly permitted Java methods should be remotely callable.

---

## 16. Suggested MVP Scope

The initial minimum viable implementation should focus on:

**Phase 1: Language and Compiler**

- Template-type declarations.
- `@Inputs(...)`, `@Code { ... }`, and `@Template { ... }` parsing.
- Java source generation.
- HTML rendering.
- Java expressions.
- Simple conditional rendering.
- Simple loops.

**Phase 2: Composition**

- Component invocation.
- Typed component parameters.
- Layout invocation.
- Named slots.
- Public/private scope resolution.
- Compile-time diagnostics.

**Phase 3: Spring Boot**

- Auto-configuration.
- Page rendering.
- Dependency injection.
- Component lifecycle.
- MVC integration.

**Phase 4: Hybrid Frontend**

- Server-side actions.
- AJAX requests.
- HTML fragment responses.
- Partial DOM updates.
- Form submission and validation.

**Phase 5: Developer Experience**

- Incremental compilation.
- Hot reload.
- IDE support.
- Source-mapped compiler errors.
- Documentation and example applications.

---

## 17. Overall Project Vision

The project aims to provide a modern frontend development experience for Java developers by combining native Java syntax, declarative HTML templates, compile-time component generation, reusable layouts, and server-driven AJAX interactions.

Rather than treating frontend development as a separate JavaScript application, the framework allows the frontend to exist naturally within the Java and Spring Boot ecosystem.

The intended developer experience is:

**Write Java + HTML in one template file → Compile into Java classes → Render HTML through Spring Boot → Update the UI through lightweight AJAX interactions.**

The framework should emphasize:

- Native Java development.
- Minimal custom syntax.
- Strong typing.
- Reusable components.
- Flexible layouts.
- Declarative template composition.
- Predictable variable scoping.
- Minimal JavaScript requirements.
- Seamless Spring Boot integration.

The immediate next architectural area to explore is the formal semantics of public component-variable exports, including name collisions, rendering order, conditional components, and interaction with the generated Java object model.

## Development Rule

- Every new production class and function must have unit test coverage in the same change.
