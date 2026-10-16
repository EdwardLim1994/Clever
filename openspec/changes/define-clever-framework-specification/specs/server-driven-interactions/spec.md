## ADDED Requirements

### Requirement: Server-side action invocation
The framework SHALL allow explicitly exposed server-side actions to be invoked by browser interactions through HTTP requests. Only actions permitted by the configured action policy SHALL be callable.

#### Scenario: Invoke an exposed action
- **WHEN** a browser interaction requests an exposed action
- **THEN** the server invokes the corresponding Java action

#### Scenario: Reject an unexposed action
- **WHEN** a request targets a Java method that is not exposed as an action
- **THEN** the server does not invoke that method

### Requirement: Partial HTML responses
The server-driven interaction layer SHALL be able to render and return updated HTML for a targeted page region without requiring a full-page response.

#### Scenario: Update a targeted region
- **WHEN** an action request completes for a targeted region
- **THEN** the server returns the rendered HTML fragment for that region

### Requirement: Browser update runtime
The browser interaction layer SHALL apply returned HTML fragments to the corresponding target region. The specific runtime and DOM update strategy remain implementation decisions.

#### Scenario: Apply fragment response
- **WHEN** the browser receives a successful fragment response for a target
- **THEN** the target region is updated with the returned HTML
