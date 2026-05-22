---
name: my-rest-client-dto
description: Whenever you are generating a objects related to an external REST API (for any REST client), than this skill should be use
---
# Naming convention
## Request and response objects naming convention
Every request body/payload should be named in the following manner:
- root request body/payload name ends with `ClientRequestDto`
- root response body name ends with `ClientResponseDto`
- every nested objects/records should have suffix `ClientDto`
## Request/response object structure
- Every request/response should be a `record` if possible.
- Every property should have `@JsonProperty("name")` to avoid API incompatibility while making code-refactoring
- Class/Record should be annotated to ignore unknown properties (for backward compatibility): `@JsonIgnoreProperties(ignoreUnknown = true)`