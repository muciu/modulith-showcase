# The Project

Showcase Spring Boot application focused on a modular monolith style, spec-first REST API design, and a small but explicit user registration flow.

The main business scenario implemented today is user registration. Registering a user also starts an email verification flow through a dedicated mailing adapter.

## Tech stack

- Java 25
- Spring Boot 4.0
- Spring Modulith
- H2 in-memory database
- Liquibase
- OpenAPI Generator
- JUnit 5
- WireMock

## What this project demonstrates

- Modular monolith structure under `com.muciociosan.theproject`
- Clear separation between functional modules and technical modules
- Spec-first REST API generation from OpenAPI files
- Database migrations managed with Liquibase
- Architecture verification with Spring Modulith and ArchUnit
- Integration testing of module behavior and external adapters

## Project layout

Main code lives under `src/main/java/com/muciociosan/theproject`.

- `applicationlogic` - application processes and orchestration, including user registration and user archiving
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

## Main use case

The central flow is `UserRegistrationProcess`, implemented in [UserRegistrationProcess.java](/Users/muciu/projects/maj-2026-interview/theproject/src/main/java/com/muciociosan/theproject/applicationlogic/userregistration/UserRegistrationProcess.java).

At a high level, the flow:

1. accepts a registration request,
2. creates the user,
3. calls the mailing adapter,
4. starts or skips email verification depending on the mail sending result.

An integration-level view of this behavior is covered by [UserRegistrationProcessItTest.java](/Users/muciu/projects/maj-2026-interview/theproject/src/test/java/com/muciociosan/theproject/applicationlogic/userregistration/UserRegistrationProcessItTest.java).

## API

The application exposes a spec-first REST API. The source contract lives in:

- [src/main/resources/openapi.yaml](/Users/muciu/projects/maj-2026-interview/theproject/src/main/resources/openapi.yaml)
- [src/main/resources/openapi/system-status.api.yaml](/Users/muciu/projects/maj-2026-interview/theproject/src/main/resources/openapi/system-status.api.yaml)
- [src/main/resources/openapi/user-registration.api.yaml](/Users/muciu/projects/maj-2026-interview/theproject/src/main/resources/openapi/user-registration.api.yaml)

Generated interfaces are added during the Gradle build and compiled from `build/generated/sources/openapi`.

Currently exposed endpoints:

- `GET /api/v1/system/status`
- `POST /api/v1/user-registration`

Example requests are available in [docs/requests.rest](/Users/muciu/projects/maj-2026-interview/theproject/docs/requests.rest).

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

## Database migrations

Liquibase changelogs live under `src/main/resources/db/changelog`.

- master changelog: [db.changelog-master.yaml](/Users/muciu/projects/maj-2026-interview/theproject/src/main/resources/db/changelog/db.changelog-master.yaml)
- SQL changesets: `src/main/resources/db/changelog/changes`

The project follows explicit migration conventions, including zero-padded numeric prefixes and preconditions for each changeset.

## Testing approach

The test suite combines several layers:

- unit tests for value objects and domain behavior
- integration tests for application flows and adapters
- architecture tests for module boundaries and package access

Integration tests use Spring Boot and mock external services instead of calling the network directly.

## Current gaps

The codebase is intentionally incomplete in a few areas:

- no every Controller is covered with MockMVC tests
- domain events are present but not fully finished (should be triggered after transaction commit)
- Outbox Pattern be implemented for sending emails to not-block main transaction
- the mail sender integration does not yet include resilience concerns
- test coverage can be expanded further
