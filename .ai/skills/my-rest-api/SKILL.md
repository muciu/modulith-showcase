---
name: my-rest-api
description: Used when defining the REST api of this service. Api will be defined using OpenApi spec and corresponding yaml files
---
# Naming convention
## Request and response objects naming convention
Every request body/payload should be named in the following manner:
- root request body/payload name ends with `ApiRequestDto`
- root response body name ends with `ApiResponseDto`
- every nested objects/records should have suffix `ApiDto`
# URL naming convention
- Every endpoint should start with `/api/v1/` unless specified explicitly
- Every URL path should have dedicated yml file that defines all supported methods (PUT, POST, GET)
- The yaml files that describes schema for the request or response should be named like : `entity-response.yaml` or `entity-request.yaml` 

# Generating API interface and implementations
Use OpenApi spec and corresponding generator to generate controller stubs.

# Supported media-types
By default application/json is used as an input and output for owned API