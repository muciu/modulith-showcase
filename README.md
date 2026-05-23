# The Project

Showcase Spring Boot application focused on a modular monolith style, spec-first REST API design, and explicit module boundaries.

The current business flow centers on user registration and user email management. Registering a user starts an email verification flow through a dedicated mailing adapter, and the API also exposes read and update operations for existing users.

## Tech stack

- Java 25
- Spring Boot 4.0.6
- Spring Modulith
- H2 in-memory database
- Liquibase
- OpenAPI Generator
- Spring Security
- Springdoc OpenAPI UI
- JUnit 5
- WireMock

## What this project demonstrates

- Modular monolith structure under `com.muciociosan.theproject`
- Clear separation between functional modules and technical modules
- Spec-first REST API generation from OpenAPI files
- Database migrations managed with Liquibase
- Spring REST client integration for the mail sender adapter
- Architecture verification with Spring Modulith and ArchUnit
- Integration and end-to-end style testing of module behavior and external adapters

## Project layout

Main code lives under `src/main/java/com/muciociosan/theproject`.

- `applicationlogic` - application processes and orchestration, including user registration, user updates, email verification, and archiving placeholders
- `users` - user domain, use cases, internal persistence, and domain events
- `adapters` - secondary adapters, currently including the mail sender client
- `infrastructure` - runtime and web concerns such as security and exception handling
- `shared` - common IDs, exceptions, converters, and shared abstractions
- `openapi` - package reserved for generated API code

Supporting folders:

- `.ai/skills` - project-specific coding conventions captured as skills
- `adrs` - architectural decisions
- `docs` - developer-facing examples such as REST calls

## Architecture notes

This codebase follows a modular monolith approach. Modules communicate through explicit interfaces and controlled package boundaries, while the root module can access all submodules when wiring the application.

Two ADRs are a good starting point:

- [adrs/0001-modules-structure.md](/Users/muciu/projects/maj-2026-interview/theproject/adrs/0001-modules-structure.md)
- [adrs/0002-ai-vs-code-conventions.md](/Users/muciu/projects/maj-2026-interview/theproject/adrs/0002-ai-vs-code-conventions.md)

For architecture verification, see [ApplicationModulesItTest](/Users/muciu/projects/maj-2026-interview/theproject/src/test/java/com/muciociosan/theproject/ApplicationModulesItTest.java).

## Main flows

The central flow is `UserRegistrationProcess`, implemented in [UserRegistrationProcess.java](/Users/muciu/projects/maj-2026-interview/theproject/src/main/java/com/muciociosan/theproject/applicationlogic/userregistration/UserRegistrationProcess.java).

At a high level, the flow:

1. accepts a registration request,
2. creates the user,
3. calls the mailing adapter,
4. starts or skips email verification depending on the mail sending result.

An integration-level view of this behavior is covered by [UserRegistrationProcessItTest.java](/Users/muciu/projects/maj-2026-interview/theproject/src/test/java/com/muciociosan/theproject/applicationlogic/userregistration/UserRegistrationProcessItTest.java).

The codebase also includes:

1. reading an existing user by id,
2. updating the current user email,
3. a fake mail sender endpoint used as a local test double for the outbound adapter.

## API

The application exposes a spec-first REST API. The source contract lives in:

- [src/main/resources/openapi.yaml](/Users/muciu/projects/maj-2026-interview/theproject/src/main/resources/openapi.yaml)
- [src/main/resources/openapi/system-status.api.yaml](/Users/muciu/projects/maj-2026-interview/theproject/src/main/resources/openapi/system-status.api.yaml)
- [src/main/resources/openapi/user-registration.api.yaml](/Users/muciu/projects/maj-2026-interview/theproject/src/main/resources/openapi/user-registration.api.yaml)
- [src/main/resources/openapi/users.api.yaml](/Users/muciu/projects/maj-2026-interview/theproject/src/main/resources/openapi/users.api.yaml)
- [src/main/resources/openapi/fake-mail-sender.api.yaml](/Users/muciu/projects/maj-2026-interview/theproject/src/main/resources/openapi/fake-mail-sender.api.yaml)

Generated interfaces are added during the Gradle build and compiled from `build/generated/sources/openapi`.

Currently exposed endpoints:

- `GET /api/v1/system/status`
- `POST /api/v1/user-registration`
- `GET /api/v1/users/{userId}`
- `PUT /api/v1/users/{userId}`
- `POST /fake/mail-sender`

Example requests are available in [docs/requests.rest](/Users/muciu/projects/maj-2026-interview/theproject/docs/requests.rest).

Swagger UI is available locally at `http://localhost:8081/swagger-ui.html`.

## Running locally

The application runs on port `8081` by default and uses an in-memory H2 database.

Compile the project:

```bash
./gradlew compileJava
```

Run all tests:

```bash
./gradlew test
```

Start the application:

```bash
./gradlew bootRun
```

Once running, try:

- `GET http://localhost:8081/api/v1/system/status`
- `POST http://localhost:8081/api/v1/user-registration`
- `GET http://localhost:8081/api/v1/users/{userId}`
- `PUT http://localhost:8081/api/v1/users/{userId}`
- `POST http://localhost:8081/fake/mail-sender`
- `http://localhost:8081/swagger-ui.html`

The default mail sender client configuration also points at the same local application instance, so the fake mail sender endpoint can be used as a safe local stub during development.

## Database migrations

Liquibase changelogs live under `src/main/resources/db/changelog`.

- master changelog: [db.changelog-master.yaml](/Users/muciu/projects/maj-2026-interview/theproject/src/main/resources/db/changelog/db.changelog-master.yaml)
- SQL changesets: `src/main/resources/db/changelog/changes`

The project follows explicit migration conventions, including zero-padded numeric prefixes and preconditions for each changeset.

## Testing approach

The test suite currently combines several layers:

- unit tests for value objects and domain behavior
- integration tests for application flows, controllers, and adapters
- end-to-end style registration flow coverage against the application context
- architecture tests for module boundaries and package access

Integration tests use Spring Boot. The outbound mail sender adapter is covered with WireMock-based tests, while local application flows use the fake endpoint or application context wiring instead of real external calls.

## Current status and gaps

The codebase is intentionally incomplete in a few areas:

- user archiving exists only as a placeholder process and scheduled job shell
- domain events are present but not yet wired to a completed after-transaction publication flow
- the outbound mail sender adapter still lacks retries and broader resilience handling
- email validation and verification metadata are still minimal in the domain model
- test coverage is solid around the main flows, but can still be expanded for the remaining edge cases and unfinished features
