## Why

Clever's direction is known, but its requirements are spread across project notes and unresolved design questions. A single OpenSpec proposal and capability specs will make the intended Java-first, compiled, server-driven framework reviewable before implementation begins.

## What Changes

- Record the confirmed framework goals, constraints, and MVP boundaries as capability specifications.
- Specify `.clever` templates, their Page/Component/Layout declarations, `@Inputs(...)` declarations, and the intended `@Code { ... }` and `@Template { ... }` blocks.
- Capture planned component, layout, slot, typed-input, and public-field export behavior while marking unresolved semantics as open decisions.
- Document the compile-to-Java pipeline, HTML escaping expectation, Spring Boot integration goals, and server-driven interaction direction.
- Define an implementation roadmap without claiming these capabilities already exist.

## Capabilities

### New Capabilities
- `template-language`: `.clever` files, template kinds, Java and template blocks, parsing, diagnostics, and generated Java classes.
- `template-composition`: typed component invocation, layouts, named slots, and component field visibility across the immediate parent boundary.
- `html-rendering`: server-side HTML generation, expression output, and safe escaping.
- `spring-boot-integration`: rendering templates through Spring Boot with request handling and dependency-injection compatibility.
- `server-driven-interactions`: declarative server actions and partial HTML responses for browser updates.

### Modified Capabilities

None. No existing OpenSpec capabilities are defined.

## Impact

- Adds OpenSpec proposal, design, task, and capability-spec artifacts; no runtime code changes.
- Establishes requirements for the existing Spring Boot / Gradle project and its ANTLR 4.13.2 parser-generation setup.
- Leaves implementation details and unresolved language semantics subject to review before implementation.
