# Clever

[![SonarQube Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=EdwardLim1994_Clever&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=EdwardLim1994_Clever)
[![SonarQube workflow](https://github.com/EdwardLim1994/Clever/actions/workflows/sonarqube.yml/badge.svg)](https://github.com/EdwardLim1994/Clever/actions/workflows/sonarqube.yml)

Clever is a Java-native, server-driven UI framework for Spring Boot. The goal is to let developers write Java and HTML in `.clever` template files, compile those files into Java classes, and render HTML without a separate JavaScript UI framework.

> **Status:** Early project setup. The repository currently contains a Spring Boot application, ANTLR build dependencies, and a small HTML tag-generation helper. The Clever template grammar, compiler, and Spring MVC integration are planned work and are not implemented yet.

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

The current build uses Spring Boot 4.1.1 and ANTLR 4.13.2. ANTLR grammars will use Gradle's standard `src/main/antlr` source directory; generated parser sources are wired into Java compilation by the Gradle ANTLR plugin.

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

macOS or Linux:

```bash
./gradlew build
./gradlew bootRun
```

Generate parser sources after adding grammar files under `src/main/antlr`:

```bash
./gradlew generateGrammarSource
```

On Windows, use `.\gradlew.bat generateGrammarSource` from PowerShell.

## Roadmap

1. Define the template grammar and diagnostics.
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
