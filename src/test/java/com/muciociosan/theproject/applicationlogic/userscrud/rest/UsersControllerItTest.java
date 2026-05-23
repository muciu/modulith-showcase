package com.muciociosan.theproject.applicationlogic.userscrud.rest;

import com.muciociosan.theproject.users.usecases.UserCreationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UsersController")
class UsersControllerItTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserCreationUseCase userCreationUseCase;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("should update user email")
    void shouldUpdateUserEmail() throws Exception {
        // given
        final var suffix = UUID.randomUUID();
        final var userId = userCreationUseCase.createUser(
                "user-" + suffix,
                "initial-" + suffix + "@example.com");

        // when
        mockMvc.perform(put("/api/v1/users/{userId}", userId.uuid())
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "updated-%s@example.com"
                                }
                                """.formatted(suffix)))
                .andExpect(status().isNoContent());

        // then
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

    @Test
    @DisplayName("should return bad request for invalid email")
    void shouldReturnBadRequestForInvalidEmail() throws Exception {
        // given
        final var suffix = UUID.randomUUID();
        final var userId = userCreationUseCase.createUser(
                "user-" + suffix,
                "initial-" + suffix + "@example.com");

        // when then
        mockMvc.perform(put("/api/v1/users/{userId}", userId.uuid())
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "not-an-email"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION"))
                .andExpect(jsonPath("$.message").value("Validation failed on field: 'email' with message: 'Not a valid email'"));
    }

    @Test
    @DisplayName("should return not found for missing user")
    void shouldReturnNotFoundForMissingUser() throws Exception {
        // given
        final var missingUserId = UUID.randomUUID();

        // when then
        mockMvc.perform(put("/api/v1/users/{userId}", missingUserId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "updated@example.com"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(startsWith("Resource of type UserAggregate and ID ")));
    }
}
