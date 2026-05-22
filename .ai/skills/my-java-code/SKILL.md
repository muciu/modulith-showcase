---
name: my-java-code
description: Used when generating or refining java code
---
While writing or refining the java code, follow those rules:
- method arguments should be marked as final
- prefer `final var` over specific typed local variables
- Don't use Setter annotation from Lombok
- Use @RequiredArgsConstructor for constructor creation
- Use java record when possible
- arguments that accepts nulls should be marked with `org.jspecify.annotations.Nullable`
- Every java class except @Service or @RestController or @UtilityClass should have equals and hashcode
- limit visibility of every class: start with package private