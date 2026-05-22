---
name: my-integration-test
description: When creaging or refining integration tests the following rules should be applied
---
# Naming convention
Integration tests should be suffixed with ItTest

# Custom properties
Integration tests uses values from properties; those values should be overwritten using the : `SpringBootTest.properties` and only necessary values should be overwritten.
Example of those, that should be overwritten are URLs to external services, databases etc.

# Other rules
External service calls should be mocked using wireMock to avoid accidental calls outside sandbox environment
Test should have the Spring Boot Context started/configured - othwerwise it is a Unit Test

# Examples
To find the reference example search for tests annotated with `@Tag("AICodingAgentExample")`