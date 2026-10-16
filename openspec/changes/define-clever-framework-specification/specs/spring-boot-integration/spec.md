## ADDED Requirements

### Requirement: Spring MVC page rendering
The Spring Boot integration SHALL allow a page template to produce an HTML HTTP response through Spring MVC.

#### Scenario: Request renders a page
- **WHEN** a request resolves to a page template
- **THEN** the integration renders the compiled page and returns its HTML response

### Requirement: Dependency injection compatibility
The runtime SHALL allow generated templates and their collaborators to use Spring-managed application services. Mutable component instances SHALL NOT be shared as singleton state by default.

#### Scenario: Template uses an application service
- **WHEN** a generated template requires a Spring-managed service
- **THEN** the integration provides that service through the configured dependency-injection mechanism

#### Scenario: Mutable component rendered for separate requests
- **WHEN** a mutable component is rendered for separate requests
- **THEN** the runtime does not implicitly share its mutable instance state between those requests

### Requirement: Compiler separation
The template parser and Java source generator SHALL be usable independently of Spring Boot-specific request handling.

#### Scenario: Compile without MVC adapter
- **WHEN** a build uses the compiler without the Spring MVC integration
- **THEN** it can parse templates and generate Java source without requiring MVC request handling
