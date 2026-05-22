---
name: my-unit-test
description: Skill should be used while agent creates or refines a Unit Test for this project
---
Some tips about creating a Unit Test are :
- every test method should have `// given`, `// when` and `// then` sections
- if the test setup is repeated, there should be created a `XyzFixtures` utility class that for example builds some standard objects / entities for the application
- test-names should be camel-case (no underscores in name)
- within a test-class logically related tests should be nested using the `@Nested` annotation from Jupiter
- Every test should have `@DisplayName`

Assertion writing tips
- when writing assertions use the assertThat from org.assertj.core.api
- use the `.satisfies(...)` whe asserting multiple related properties (like item on a list or nested object)