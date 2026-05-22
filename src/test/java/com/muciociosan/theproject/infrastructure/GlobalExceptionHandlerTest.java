package com.muciociosan.theproject.infrastructure;

import com.muciociosan.theproject.openapi.generated.model.ErrorApiResponseDto;
import com.muciociosan.theproject.shared.exceptions.ApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Nested
    @DisplayName("handleApplicationException")
    class HandleApplicationException {

        @Test
        @DisplayName("should map application exception to bad request")
        void shouldMapApplicationExceptionToBadRequest() {
            // given
            final ApplicationException exception = new ApplicationException("INVALID_INPUT", "Validation failed");

            // when
            final ResponseEntity<ErrorApiResponseDto> result =
                globalExceptionHandler.handleApplicationExceptionWith500(exception);

            // then
            assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode()),
                () -> assertEquals("INVALID_INPUT", result.getBody().getCode()),
                () -> assertEquals("Validation failed", result.getBody().getMessage()),
                () -> assertNull(result.getBody().getDetails())
            );
        }
    }
}
