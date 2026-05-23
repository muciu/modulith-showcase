package com.muciociosan.theproject.users.domain;

import com.muciociosan.theproject.shared.exceptions.InvalidStateException;
import com.muciociosan.theproject.users.usecases.EmailValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserAggregate")
class UserAggregateTest {

    @Nested
    @DisplayName("creation")
    class Creation {

        @Test
        @DisplayName("should expose initial user view and pending user created event")
        void shouldExposeInitialUserViewAndPendingUserCreatedEvent() {
            // given
            final var username = UsernameValue.from("user-" + UUID.randomUUID());
            final var email = "john-" + UUID.randomUUID() + "@example.com";

            // when
            final var aggregate = UserAggregate.newFrom(username, email);
            final var view = aggregate.view();
            final var events = aggregate.resetPendingEvents();

            // then
            assertThat(view).satisfies(userView -> {
                assertThat(userView.username()).isEqualTo(username.value());
                assertThat(userView.email()).satisfies(userEmail -> {
                    assertThat(userEmail.email()).isEqualTo(EmailValue.from(email));
                    assertThat(userEmail.isVerified()).isFalse();
                });
            });
            assertThat(events)
                    .singleElement()
                    .isInstanceOfSatisfying(UserCreated.class, userCreated ->
                            assertThat(userCreated.userId()).isEqualTo(aggregate.id()));
        }

        @Test
        @DisplayName("should clear pending events after reset")
        void shouldClearPendingEventsAfterReset() {
            // given
            final var aggregate = UserAggregate.newFrom(
                    UsernameValue.from("user-" + UUID.randomUUID()),
                    "john-" + UUID.randomUUID() + "@example.com"
            );

            // when
            final var firstReset = aggregate.resetPendingEvents();
            final var secondReset = aggregate.resetPendingEvents();

            // then
            assertThat(firstReset).hasSize(1);
            assertThat(secondReset).isEmpty();
        }
    }

    @Nested
    @DisplayName("verification flow")
    class VerificationFlow {

        @Test
        @DisplayName("should mark verification as started for matching email")
        void shouldMarkVerificationAsStartedForMatchingEmail() {
            // given
            final var email = "john-" + UUID.randomUUID() + "@example.com";
            final var aggregate = UserAggregate.newFrom(UsernameValue.from("user-" + UUID.randomUUID()), email);

            // when
            aggregate.emailVerificationStarted(EmailValue.from(email));

            // then
            assertThat(emailEntityFor(aggregate, email).verificationStatus())
                    .isEqualTo(UserEmailEntity.VerificationStatus.VERIFICATION_STARTED);
        }

        @Test
        @DisplayName("should mark updated email as verified and current after verification started")
        void shouldMarkUpdatedEmailAsVerifiedAndCurrentAfterVerificationStarted() {
            // given
            final var aggregate = UserAggregate.newFrom(
                    UsernameValue.from("user-" + UUID.randomUUID()),
                    "initial-" + UUID.randomUUID() + "@example.com"
            );
            final var updatedEmail = "updated-" + UUID.randomUUID() + "@example.com";
            aggregate.updateEmail(EmailValue.from(updatedEmail));
            aggregate.emailVerificationStarted(EmailValue.from(updatedEmail));

            // when
            aggregate.markEmailVerified(EmailValue.from(updatedEmail));

            // then
            assertThat(emailEntityFor(aggregate, updatedEmail)).satisfies(emailEntity -> {
                assertThat(emailEntity.verificationStatus()).isEqualTo(UserEmailEntity.VerificationStatus.VERIFIED);
                assertThat(emailEntity.current()).isTrue();
                assertThat(emailEntity.verificationDate()).isNotNull();
            });
            assertThat(aggregate.view().email()).satisfies(userEmail -> {
                assertThat(userEmail.email()).isEqualTo(EmailValue.from(updatedEmail));
                assertThat(userEmail.isVerified()).isTrue();
            });
        }

        @Test
        @DisplayName("should reject verification start for missing email")
        void shouldRejectVerificationStartForMissingEmail() {
            // given
            final var aggregate = UserAggregate.newFrom(
                    UsernameValue.from("user-" + UUID.randomUUID()),
                    "john-" + UUID.randomUUID() + "@example.com"
            );
            final var missingEmail = EmailValue.from("missing-" + UUID.randomUUID() + "@example.com");

            // when / then
            assertThatThrownBy(() -> aggregate.emailVerificationStarted(missingEmail))
                    .isInstanceOf(InvalidStateException.class)
                    .hasMessage("No email to verify");
        }

        @Test
        @DisplayName("should reject marking email as verified when verification was not started")
        void shouldRejectMarkingEmailAsVerifiedWhenVerificationWasNotStarted() {
            // given
            final var email = "john-" + UUID.randomUUID() + "@example.com";
            final var aggregate = UserAggregate.newFrom(UsernameValue.from("user-" + UUID.randomUUID()), email);

            // when / then
            assertThatThrownBy(() -> aggregate.markEmailVerified(EmailValue.from(email)))
                    .isInstanceOf(InvalidStateException.class)
                    .hasMessage("No email to verify");
        }
    }

    private static UserEmailEntity emailEntityFor(final UserAggregate aggregate, final String email) {
        return emailEntitiesOf(aggregate).stream()
                .filter(userEmailEntity -> userEmailEntity.email().equals(EmailValue.from(email)))
                .findFirst()
                .orElseThrow();
    }

    @SuppressWarnings("unchecked")
    private static Set<UserEmailEntity> emailEntitiesOf(final UserAggregate aggregate) {
        try {
            final Field emailsField = UserAggregate.class.getDeclaredField("emails");
            emailsField.setAccessible(true);
            return (Set<UserEmailEntity>) emailsField.get(aggregate);
        } catch (final ReflectiveOperationException exception) {
            throw new AssertionError("Unable to inspect aggregate emails", exception);
        }
    }
}
