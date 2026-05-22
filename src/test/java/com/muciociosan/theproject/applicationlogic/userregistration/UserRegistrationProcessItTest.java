package com.muciociosan.theproject.applicationlogic.userregistration;

import com.muciociosan.theproject.adapters.MailingClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayName("UserRegistrationProcess")
class UserRegistrationProcessItTest {

    @Autowired
    private UserRegistrationProcess userRegistrationProcess;

    @Autowired
    private MailingClient mailingClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void resetMocks() {
        reset(mailingClient);
    }

    @Test
    @DisplayName("should not start verification when email sending fails")
    void shouldNotStartVerificationWhenEmailSendingFails() {
        // given
        final var suffix = UUID.randomUUID();
        final var username = "user-" + suffix;
        final var email = "user-" + suffix + "@example.com";
        when(mailingClient.sendEmail("Please verify", "Link to finish verification", email))
                .thenReturn(new MailingClient.SendingResult(false, "SMTP unavailable"));

        // when
        final var userId = userRegistrationProcess.registerUser(username, email);

        // then
        assertThat(emailVerificationStatusFor(userId.uuid())).isEqualTo("NEW");
        assertThat(verifiedEmailCountFor(userId.uuid())).isZero();
        verify(mailingClient).sendEmail("Please verify", "Link to finish verification", email);
    }

    @Test
    @DisplayName("should start verification when email sending succeeds")
    void shouldStartVerificationWhenEmailSendingSucceeds() {
        // given
        final var suffix = UUID.randomUUID();
        final var username = "user-" + suffix;
        final var email = "user-" + suffix + "@example.com";
        when(mailingClient.sendEmail("Please verify", "Link to finish verification", email))
                .thenReturn(new MailingClient.SendingResult(true, null));

        // when
        final var userId = userRegistrationProcess.registerUser(username, email);

        // then
        assertThat(emailVerificationStatusFor(userId.uuid())).isEqualTo("VERIFICATION_STARTED");
        assertThat(verifiedEmailCountFor(userId.uuid())).isZero();
        verify(mailingClient).sendEmail("Please verify", "Link to finish verification", email);
    }

    private String emailVerificationStatusFor(final UUID userId) {
        return jdbcTemplate.queryForObject(
                """
                SELECT validation_status
                FROM user_emails
                WHERE user_id = ?
                """,
                String.class,
                userId
        );
    }

    private Integer verifiedEmailCountFor(final UUID userId) {
        return jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_emails
                WHERE user_id = ?
                  AND verification_date IS NOT NULL
                """,
                Integer.class,
                userId
        );
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        MailingClient mailingClient() {
            return mock(MailingClient.class);
        }
    }
}
