package com.muciociosan.theproject.e2e;

import com.muciociosan.theproject.shared.exceptions.InvalidStateException;
import com.muciociosan.theproject.shared.ids.UserId;
import com.muciociosan.theproject.users.usecases.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayName("EmailUpdateUseCase")
class RegistrationAndConfirmationE2EItTest {

    @Autowired
    private UserCreationUseCase userCreationUseCase;

    @Autowired
    private EmailUpdateUseCase emailUpdateUseCase;

    @Autowired
    private UserLookupUseCase userLookupUseCase;

    @Autowired
    private EmailVerificationUseCase emailVerificationUseCase;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("should create user, verify email then update email and verify new email again")
    void shouldPersistNewEmailForExistingUser() {
        // given
        final var suffix = UUID.randomUUID();
        final var username = "user-" + suffix;
        final var initialEmail = EmailValue.from("initial-" + suffix + "@example.com");
        final UserId userId = userCreationUseCase.createUser(username, initialEmail.value());
        assertThat(userLookupUseCase.findUser(username)).isPresent();

        // then
        assertThatThrownBy(() -> emailVerificationUseCase.verificationCompleted(userId, initialEmail))
                .withFailMessage("Verification must be started before it is marked completed!")
                .isInstanceOf(InvalidStateException.class)
                .hasMessage("No email to verify");

        // when
        emailVerificationUseCase.markVerificationStarted(userId, initialEmail);
        emailVerificationUseCase.verificationCompleted(userId, initialEmail);
        final var updatedEmailUser = userLookupUseCase.findUser(username).orElseThrow();

        // then
        assertThat(updatedEmailUser).satisfies(user -> {
            assertThat(user.email().isVerified()).isTrue();
            assertThat(user.email().email().value()).isEqualTo(initialEmail.value());
        });
        assertThatThrownBy(() -> emailVerificationUseCase.verificationCompleted(userId, initialEmail))
                .isInstanceOf(InvalidStateException.class)
                .hasMessage("No email to verify");

        // when
        final var updatedEmail = EmailValue.from("updated-" + suffix + "@example.com");
        emailUpdateUseCase.updateEmail(userId, updatedEmail);

        // then
        final var afterUpdate = userLookupUseCase.getUser(userId);
        assertThat(afterUpdate.email()).withFailMessage("Recent verified email should be used").satisfies(email -> {
            assertThat(email.email().equals(EmailValue.from(initialEmail.value())));
            assertThat(email.isVerified()).isTrue();
        });

        // when
        emailVerificationUseCase.markVerificationStarted(userId, updatedEmail);
        emailVerificationUseCase.verificationCompleted(userId, updatedEmail);

        // then
        final var userAfterSecondUpdate = userLookupUseCase.findUser(username).orElseThrow();
        assertThat(userAfterSecondUpdate.email()).withFailMessage("New updated and verified is used").satisfies(email -> {
            assertThat(email.email().equals(EmailValue.from(updatedEmail.value())));
            assertThat(email.isVerified()).isTrue();
        });

        // and - data structure check
        final var emailRows = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_emails
                WHERE user_id = ?
                """,
                Integer.class,
                userId.uuid()
        );
        assertThat(emailRows).isEqualTo(2);
    }
}
