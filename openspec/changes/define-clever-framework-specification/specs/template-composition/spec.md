## ADDED Requirements

### Requirement: Typed component invocation
The language SHALL allow a template to invoke a component by its generated Java class and pass arguments corresponding to the component's declared `@Inputs(...)`. The compiler SHALL validate the number and Java types of supplied arguments.

#### Scenario: Valid component invocation
- **WHEN** a template invokes a component with arguments matching its declared inputs
- **THEN** the compiler generates a typed component rendering call

#### Scenario: Invalid component arguments
- **WHEN** a component invocation has the wrong number or incompatible types of arguments
- **THEN** the compiler reports a validation diagnostic at the invocation

### Requirement: Layout slots
The language SHALL support layouts with named slot insertion points and invocations that provide named slot content. Slot content SHALL support template content, Java expressions, and component invocations.

#### Scenario: Supplied named slot
- **WHEN** a page invokes a layout and supplies content for a declared slot
- **THEN** rendering inserts that content at the matching layout slot

#### Scenario: Slot reference validation
- **WHEN** a layout invocation refers to a slot name
- **THEN** the compiler validates the slot reference against the layout's declared slots

### Requirement: Component field visibility
Private component fields SHALL remain internal to the declaring component. Public component fields SHALL be eligible for access by the immediate parent template after that component is invoked, without requiring a separate reference annotation.

#### Scenario: Parent reads public child field
- **WHEN** a component declares a public field and its parent invokes the component
- **THEN** the parent template can reference that field after the invocation

#### Scenario: Parent cannot read private child field
- **WHEN** a component declares a private field and its parent invokes the component
- **THEN** the parent template cannot access that field through component export semantics

### Requirement: Export scope boundary
Component field exports SHALL be limited to the immediate parent template and SHALL NOT automatically propagate through all ancestors.

#### Scenario: Nested component export
- **WHEN** a component invokes a child that has public fields
- **THEN** those fields are available to the invoking component, but are not automatically re-exported to the invoking component's parent
