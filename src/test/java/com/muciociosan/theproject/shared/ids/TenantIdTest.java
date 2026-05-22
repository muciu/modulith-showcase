package com.muciociosan.theproject.shared.ids;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("TenantId")
class TenantIdTest {

    @Nested
    @DisplayName("equals and hashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("should be equal and have same hashCode for the same UUID")
        void shouldBeEqualAndHaveSameHashCodeForTheSameUuid() {
            // given
            final UUID uuid = UUID.fromString("f7c2f245-7cb6-4f41-97f8-0a5984c1694d");
            final TenantId firstTenantId = TenantId.from(uuid);
            final TenantId secondTenantId = TenantId.from(uuid);

            // when

            // then
            assertAll(
                () -> assertEquals(firstTenantId, secondTenantId),
                () -> assertEquals(firstTenantId.hashCode(), secondTenantId.hashCode())
            );
        }

        @Test
        @DisplayName("should not be equal for different UUID values")
        void shouldNotBeEqualForDifferentUuidValues() {
            // given
            final TenantId firstTenantId = TenantId.from(UUID.fromString("f7c2f245-7cb6-4f41-97f8-0a5984c1694d"));
            final TenantId secondTenantId = TenantId.from(UUID.fromString("2209ab80-a464-450c-bf7e-a2dd340ff230"));

            // when

            // then
            assertNotEquals(firstTenantId, secondTenantId);
        }

        @Test
        @DisplayName("should not be equal to a different ID type with the same UUID")
        void shouldNotBeEqualToADifferentIdTypeWithTheSameUuid() {
            // given
            final UUID uuid = UUID.fromString("f7c2f245-7cb6-4f41-97f8-0a5984c1694d");
            final TenantId tenantId = TenantId.from(uuid);
            final UserId userId = UserId.from(uuid);

            // when

            // then
            assertNotEquals(tenantId, userId);
        }
    }
}
