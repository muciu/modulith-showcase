package com.muciociosan.theproject.shared.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApplicationException")
class ApplicationExceptionTest {

    @Nested
    @DisplayName("valid code")
    class ValidCode {

        @DisplayName("should allow uppercase codes that start and end with a letter")
        @ParameterizedTest(name = "[{index}] code={0}")
        @ValueSource(strings = {
            "A",
            "USER_NOT_FOUND",
            "ERROR_404_CODE",
            "ABC123Z",
            "A_B_C"
        })
        void shouldAllowUppercaseCodesThatStartAndEndWithLetter(final String code) {
            // given

            // when

            // then
            assertDoesNotThrow(() -> new ApplicationException(code, "Fixed message"));
        }
    }

    @Nested
    @DisplayName("invalid code")
    class InvalidCode {

        @DisplayName("should reject invalid code formats")
        @ParameterizedTest(name = "[{index}] code={0}")
        @NullAndEmptySource
        @ValueSource(strings = {
            " ",
            "user_not_found",
            "_USER_NOT_FOUND",
            "USER_NOT_FOUND_",
            "1USER_NOT_FOUND",
            "USER_NOT_FOUND1",
            "USER-NOT-FOUND",
            "USER NOT FOUND"
        })
        void shouldRejectInvalidCodeFormats(final String code) {
            // given

            // when
            final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new ApplicationException(code, "fixed message"));

            // then
            assertEquals(
                "Code must contain only uppercase letters, digits, and underscores, and start and end with a letter.",
                exception.getMessage()
            );
        }
    }
}
