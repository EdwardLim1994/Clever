## Context

Clever is an early Java 25 / Spring Boot project. Its current runtime is a Spring Boot application; its Gradle build is configured with ANTLR 4.13.2, but there is no `.clever` grammar, compiler, renderer, or framework runtime yet.

The intended framework compiles Java-and-HTML template files into Java classes. Spring Boot serves rendered pages and later handles server actions that return partial HTML. This design captures the direction already agreed while leaving unsettled language and lifecycle details open for review.

## Goals / Non-Goals

**Goals:**

- Define a compile-time, Java-first template language using `.clever` files.
- Specify the initial Page, Component, and Layout concepts and intended template composition.
- Keep parsing and code generation separable from Spring Boot integration.
- Define safe server-side HTML output and a path to server-driven partial updates.
- Provide a reviewable starting contract for iterative refinement before implementation.

**Non-Goals:**

- Claim that compiler, runtime, action dispatch, or browser integration already exists.
- Finalize exact expression/directive grammar, routing conventions, or AJAX attributes.
- Specify persistent component state, authorization policy, or a full browser virtual DOM.
- Implement a complete production framework in this specification change.

## Decisions

1. **Use ANTLR for the template grammar.** ANTLR is already configured in Gradle and supports a structured grammar for mixed Java and markup. Regex-only parsing is unsuitable for nested Java expressions, strings, comments, and blocks. The exact grammar and parser stages remain implementation work.
2. **Compile templates to Java classes.** Generated Java preserves Java typing and lets the normal Java compiler validate generated output. Runtime template interpretation is not the intended rendering path.
3. **Treat `.clever` as the template extension.** Gradle's ANTLR plugin reads compiler grammars from `src/cleverCompiler/antlr`; `.clever` discovery and compilation will be added to the Clever compiler workflow.
4. **Separate language/compiler concerns from Spring Boot adapters.** The parser and source generator should be usable without Spring MVC. Spring integration owns request handling, template lookup, and HTTP responses.
5. **Use an output writer and escape dynamic text by default.** Static markup is written as markup; expression output is escaped. A raw/trusted HTML escape hatch is not part of the initial confirmed design.
6. **Separate template inputs from Java code blocks.** `@Inputs(Type name, ...)` declares a component or layout's typed inputs; the compiler generates constructor parameters and fields from that declaration. `@Code { ... }` contains Java members, while `@Template { ... }` contains template content. This keeps the public component API explicit and leaves custom Java constructors available inside `@Code` if needed.
7. **Keep the interaction layer server-driven.** An action request invokes permitted server-side behavior and returns rendered HTML for a targeted browser update. The concrete action registration and DOM update protocol remain open.
8. **Use `@(JavaExpression)` for the first interpolation slice.** The compiler recognizes this form in HTML text nodes and HTML-escapes its result. Attribute expressions, control flow, component calls, and layouts remain outside this slice.

## Risks / Trade-offs

- [Mixed Java and HTML grammar is complex] → Start with a deliberately small grammar and add syntax only with parser and diagnostic coverage.
- [Generated Java errors can be hard to map back to templates] → Preserve source locations through parsing and emit diagnostics against the `.clever` source.
- [Public component field exports are nonstandard Java scoping] → Limit the confirmed intent to the immediate parent and settle ambiguity and conditional-render semantics before implementation.
- [Mutable component state can leak between requests if instances are shared] → Do not treat mutable components as singleton state; finalize instance creation and persistence rules before action support.
- [Dynamic output can create injection bugs] → Escape dynamic text by default and define any trusted HTML boundary explicitly.
- [Server actions expand the security surface] → Require explicit action exposure and rely on Spring Security / CSRF protections; exact policy remains to be specified.

## Migration Plan

No existing template users or framework runtime exist, so no migration is required. Add the language/compiler first, then composition, Spring rendering, and server-driven interactions in separate implementation steps. Existing Spring Boot application behavior should remain runnable throughout.

## Open Questions

- What are the exact expression, conditional, loop, comment, and escaping syntaxes?
- How are Java package names and imports represented in `.clever` files?
- Are generated `@Inputs` fields final, and how do input defaults interact with component construction?
- How do Spring dependency injection and user-authored Java constructors interact with generated input constructors?
- How are public field exports named, typed, and made available after conditional invocations?
- What is the diagnostic behavior for duplicate exports, and do exports propagate through layouts?
- Are layout slots required, can they have fallback content, and how does nested layout composition work?
- How do pages map to routes and bind request parameters?
- How are actions registered, authorized, protected against CSRF, and associated with a component instance or persisted state?
- Does the browser layer use HTMX or a small Clever-owned runtime?
- What Java/source mapping and incremental compilation behavior is required for the first compiler release?
