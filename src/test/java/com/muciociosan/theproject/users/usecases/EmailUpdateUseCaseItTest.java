package com.muciociosan.theproject.users.usecases;

import com.muciociosan.theproject.shared.ids.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayName("EmailUpdateUseCase")
class EmailUpdateUseCaseItTest {

    @Autowired
    private UserCreationUseCase userCreationUseCase;

    @Autowired
    private EmailUpdateUseCase emailUpdateUseCase;

    @Autowired
    private UserLookupUseCase userLookupUseCase;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("should persist new email for existing user")
    void shouldPersistNewEmailForExistingUser() {
        // given
        final var suffix = UUID.randomUUID();
        final var username = "user-" + suffix;
        final var initialEmail = "initial-" + suffix + "@example.com";
        final var updatedEmail = "updated-" + suffix + "@example.com";
        final UserId userId = userCreationUseCase.createUser(username, initialEmail);
        assertThat(userLookupUseCase.findUser(username)).isPresent();

        // when
        emailUpdateUseCase.updateEmail(userId, EmailValue.from(updatedEmail));

        // then
        final var afterUpdate = userLookupUseCase.getUser(userId);
        assertThat(afterUpdate.email()).satisfies(email -> {
            assertThat(email.email().equals(EmailValue.from(initialEmail)));
            assertThat(email.isVerified()).isFalse();
        });
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
