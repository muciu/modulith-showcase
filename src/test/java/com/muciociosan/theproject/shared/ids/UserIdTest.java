package com.muciociosan.theproject.shared.ids;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@DisplayName("UserId")
class UserIdTest {

    @Nested
    @DisplayName("from")
    class From {

        @Test
        @DisplayName("should create user ID from UUID")
        void shouldCreateUserIdFromUuid() {
            // given
            final UUID uuid = UUID.fromString("7fca371e-c5dc-42d0-b01e-8d7f33517d6e");

            // when
            final UserId userId = UserId.from(uuid);

            // then
            assertThat(userId)
                .isNotNull()
                .satisfies(createdUserId -> {
                    assertThat(createdUserId.value()).isEqualTo(uuid);
                    assertThat(createdUserId.uuid()).isEqualTo(uuid);
                    assertThat(createdUserId.toString()).isEqualTo(uuid.toString());
                });
        }

        @Test
        @DisplayName("should reject null UUID")
        void shouldRejectNullUuid() {
            // given
            final UUID uuid = null;

            // when

            // then
            assertThatNullPointerException()
                .isThrownBy(() -> UserId.from(uuid))
                .withMessage("value must not be null!");
        }
    }

    @Nested
    @DisplayName("fromNullable")
    class FromNullable {

        @Test
        @DisplayName("should return null for null UUID")
        void shouldReturnNullForNullUuid() {
            // given
            final UUID uuid = null;

            // when
            final UserId userId = UserId.fromNullable(uuid);

            // then
            assertThat(userId).isNull();
        }

        @Test
        @DisplayName("should create user ID for non-null UUID")
        void shouldCreateUserIdForNonNullUuid() {
            // given
            final UUID uuid = UUID.fromString("fd78f3cb-b6a8-4f42-b7f7-97d19eb6efb2");

            // when
            final UserId userId = UserId.fromNullable(uuid);

            // then
            assertThat(userId)
                .isNotNull()
                .satisfies(createdUserId -> assertThat(createdUserId.value()).isEqualTo(uuid));
        }
    }

    @Nested
    @DisplayName("equals and hashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("should be equal and have same hashCode for the same UUID")
        void shouldBeEqualAndHaveSameHashCodeForTheSameUuid() {
            // given
            final UUID uuid = UUID.fromString("f7c2f245-7cb6-4f41-97f8-0a5984c1694d");
            final UserId firstUserId = UserId.from(uuid);
            final UserId secondUserId = UserId.from(uuid);

            // when

            // then
            assertThat(firstUserId)
                .isEqualTo(secondUserId)
                .hasSameHashCodeAs(secondUserId);
        }

        @Test
        @DisplayName("should not be equal for different UUID values")
        void shouldNotBeEqualForDifferentUuidValues() {
            // given
            final UserId firstUserId = UserId.from(UUID.fromString("f7c2f245-7cb6-4f41-97f8-0a5984c1694d"));
            final UserId secondUserId = UserId.from(UUID.fromString("2209ab80-a464-450c-bf7e-a2dd340ff230"));

            // when

            // then
            assertThat(firstUserId).isNotEqualTo(secondUserId);
        }

        @Test
        @DisplayName("should not be equal to a different ID type with the same UUID")
        void shouldNotBeEqualToADifferentIdTypeWithTheSameUuid() {
            // given
            final UUID uuid = UUID.fromString("f7c2f245-7cb6-4f41-97f8-0a5984c1694d");
            final UserId userId = UserId.from(uuid);
            final TenantId tenantId = TenantId.from(uuid);

            // when

            // then
            assertThat(userId).isNotEqualTo(tenantId);
        }
    }
}
