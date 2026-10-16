## 1. Finalize language contract

- [ ] 1.1 Resolve the open syntax decisions in `design.md` and update `template-language` requirements.
- [ ] 1.2 Specify component export collisions, conditional invocation behavior, and slot fallback rules.
- [ ] 1.3 Specify construction, dependency injection, page routing, action security, and state lifecycle.

## 2. Build the compiler foundation

- [ ] 2.1 Add the initial ANTLR grammar for `.clever` declarations and primary blocks.
- [ ] 2.2 Implement source diagnostics that retain original file and location information.
- [ ] 2.3 Build semantic validation for template types and typed `@Inputs(...)` declarations.
- [ ] 2.4 Generate Java classes and connect generated sources to Gradle compilation.

## 3. Implement template rendering and composition

- [ ] 3.1 Generate HTML rendering code for static markup and Java expressions.
- [ ] 3.2 Escape dynamic text by default and verify escaping behavior.
- [ ] 3.3 Add conditionals and iteration after their grammar is finalized.
- [ ] 3.4 Add typed component invocation and argument validation.
- [ ] 3.5 Add layouts, named slots, nested composition, and slot validation.
- [ ] 3.6 Add immediate-parent public field exports and enforce private-field isolation.

## 4. Integrate with Spring Boot

- [ ] 4.1 Add a Spring MVC adapter that resolves and renders compiled page templates.
- [ ] 4.2 Integrate Spring-managed services without sharing mutable component state by default.
- [ ] 4.3 Add request-to-page routing and request parameter binding after their rules are specified.

## 5. Add server-driven interactions

- [ ] 5.1 Define and implement explicit action registration and security checks.
- [ ] 5.2 Render targeted component or page regions as HTML fragment responses.
- [ ] 5.3 Add the browser update runtime and verify targeted DOM updates.
