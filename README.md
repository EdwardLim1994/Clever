# Clever

[![SonarQube Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=EdwardLim1994_Clever&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=EdwardLim1994_Clever)
[![SonarQube workflow](https://github.com/EdwardLim1994/Clever/actions/workflows/sonarqube.yml/badge.svg)](https://github.com/EdwardLim1994/Clever/actions/workflows/sonarqube.yml)

Clever is a Java-native, server-driven UI framework for Spring Boot. The goal is to let developers write Java and HTML in `.clever` template files, compile those files into Java classes, and render HTML without a separate JavaScript UI framework.

> **Status:** Early implementation. A compiler generates Java for one `.clever` file using `@Code` and `@Template`. The sample page is compiled at build time and rendered by a REST endpoint; components and layouts are not implemented yet.

## Project direction

Clever combines ideas from Razor, Blade, HTMX, and Spring Boot:

- Java code and HTML templates in one file.
- Compile-time parsing and Java source generation.
- Reusable pages, components, layouts, and named slots.
- Strongly typed component inputs.
- Server-rendered HTML with optional partial updates over HTTP.
- Spring Boot integration for routing, dependency injection, and application services.

## Planned template syntax

The syntax below describes the intended direction; it is not supported by the current application yet.

```java
@Component UserCard

@Inputs(String name, int age)

@Code {
    private String displayName() {
        return name.toUpperCase();
    }
}

@Template {
    <article>
        <h2>@displayName()</h2>
        <p>Age: @age</p>
    </article>
}
```

Templates will declare a type (`@Page`, `@Component`, or `@Layout`) and a class name. The compiler is intended to generate Java classes that render HTML. Component calls and layout slots are also part of the design, but their grammar and semantics are still being defined.

## Planned architecture

```text
.clever template
  → ANTLR lexer and parser
  → template syntax tree
  → validation and Java source generation
  → Java compilation
  → Spring Boot rendering
```

The planned compiler will live independently from the Spring Boot adapter where practical. A lightweight browser runtime may later handle server-returned HTML fragments for interactive updates.

## Requirements

- Java 25
- Gradle Wrapper (included)

The current build uses Spring Boot 4.1.1 and ANTLR 4.13.2. Compiler grammars live under `src/cleverCompiler/antlr`; Gradle generates parser sources before compiling the compiler.

## SonarQube Cloud

The `SonarQube` GitHub Actions workflow builds the project, uploads an analysis to SonarQube Cloud, and waits for the Quality Gate result. Create/import this repository in SonarQube Cloud with project key `EdwardLim1994_Clever`, then configure:

- Repository secret `SONAR_TOKEN` with Execute Analysis permission.
- Repository variable `SONAR_ORGANIZATION` with the SonarQube Cloud organization key.

The workflow runs on pushes, pull requests from the same repository, and manual dispatch. Pull requests from forks skip analysis because GitHub does not expose repository secrets to them. The badges above show the latest Quality Gate and workflow run; they populate after the first successful analysis.

Make the SonarQube Cloud project public if the Quality Gate badge should be visible to readers without SonarQube access.

## Build and run

Windows PowerShell:

```powershell
.\gradlew.bat build
.\gradlew.bat bootRun
```

Open `http://localhost:8080/` to render the sample `.clever` page with the default name `world`, or pass `?param=name`. The `param` value is HTML-escaped before it is returned.

macOS or Linux:

```bash
./gradlew build
./gradlew bootRun
```

Generate parser sources after adding grammar files under `src/cleverCompiler/antlr`:

```bash
./gradlew generateCleverCompilerGrammarSource
```

On Windows, use `.\gradlew.bat generateCleverCompilerGrammarSource` from PowerShell.

Generate and compile the sample template with the application. By default, `bootRun`, `test`, and `bootJar` compile `src/main/clever/HelloPage.clever` into the application.

```powershell
.\gradlew.bat compileCleverJava
```

The `cleverSource`, `cleverPackage`, and `cleverOutput` Gradle properties can change the generated template. Keep the generated package and class name aligned with Java imports such as `ExampleController`'s `HelloPage` import.

Template text expressions use `@(JavaExpression)` and are HTML-escaped by default. Expressions in attributes are not supported yet.

`CleverHtml` sanitizes the final response with a jsoup allowlist. Scripts, event handlers, unsafe URLs, and unsupported tags or attributes are removed. Keep template markup within the allowed HTML elements and attributes; expand the allowlist deliberately when adding new template features.

Render a compiled template by calling its generated constructor:

```java
CleverHtml html = CleverRenderer.render(new HelloPage("Julian Bashir"));
```

The sample endpoint uses the same constructor-based API. The class and map overloads remain available for templates selected at runtime.

## Roadmap

1. Refine the initial template grammar and diagnostics.
2. Parse `.clever` files and generate Java classes.
3. Add HTML rendering, expressions, conditionals, and loops.
4. Add typed components, layouts, slots, and scope validation.
5. Integrate rendering with Spring Boot.
6. Add server actions and partial HTML updates.

## Design notes

- `.clever` is the intended template file extension.
- Public component fields are intended to be exposed to the immediate parent template; exact scope and collision rules remain to be specified.
- Component state is not assumed to persist between HTTP requests. Persistent state should be managed explicitly through application services, sessions, or a state store.
- Dynamic HTML values should be escaped by default.
