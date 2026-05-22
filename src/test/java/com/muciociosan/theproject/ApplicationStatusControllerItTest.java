package com.muciociosan.theproject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ApplicationStatusController")
class ApplicationStatusControllerItTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("should return application status when no type is provided")
    void shouldReturnApplicationStatusWhenNoTypeIsProvided() throws Exception {
        mockMvc.perform(get("/api/v1/system/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.applicationName").value("the-project"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("should return bad request for validation type")
    void shouldReturnBadRequestForValidationType() throws Exception {
        mockMvc.perform(get("/api/v1/system/status").queryParam("type", "validation"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION"))
            .andExpect(jsonPath("$.message").value("Validation failed on field: 'systemStatus' with message: 'Test purpose system status error!'"));
    }

    @Test
    @DisplayName("should return bad request for not found type")
    void shouldReturnBadRequestForNotFoundType() throws Exception {
        mockMvc.perform(get("/api/v1/system/status").queryParam("type", "not_found"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
            .andExpect(jsonPath("$.message").value(startsWith("Resource of type String and ID ")));
    }

    @Test
    @DisplayName("should return internal server error for internal type")
    void shouldReturnInternalServerErrorForInternalType() throws Exception {
        mockMvc.perform(get("/api/v1/system/status").queryParam("type", "internal"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.message").value("Internal error!"));
    }
}
