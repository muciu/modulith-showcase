package com.muciociosan.theproject.users.mailsenderclient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.muciociosan.theproject.adapters.mailsenderclient.MailSenderRestClient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("AICodingAgentExample")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "theproject.clients.mailsender.url=${theproject.ittest.mailsender.url}",
        "theproject.clients.mailsender.api-key=test-api-key",
        "theproject.clients.mailsender.api-key-header-name=x-api-testing-key",
    }
)
@DisplayName("MailSenderRestClient")
class MailSenderRestClientItTest {

    private static final WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());

    static {
        wireMockServer.start();
        System.setProperty("theproject.ittest.mailsender.url", wireMockServer.baseUrl());
    }

    @Autowired
    private MailSenderRestClient mailSenderRestClient;

    @BeforeEach
    void resetWireMock() {
        wireMockServer.resetAll();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
        System.clearProperty("test.mailsender.url");
    }

    @Test
    @DisplayName("should fetch user data from external service")
    void shouldFetchUserDataFromExternalService() {
        // given
        wireMockServer.stubFor(
            get(urlEqualTo("/api/collections/send-email/records?project_id=23618"))
                .withHeader("x-api-testing-key", equalTo("test-api-key"))
                .willReturn(
                    okJson(
                        """
                         {
                           "data": {
                             "id": "979f88cd-acfc-41f1-b5d9-e391363baec6",
                             "collection_id": "48c4c730-7a2a-4dd0-a645-e0291b472938",
                             "project_id": 23618,
                             "app_user_id": null,
                             "created_by": 117429,
                             "created_at": "2026-05-21T11:01:10.057Z",
                             "updated_at": "2026-05-21T11:01:10.057Z",
                             "deleted_at": null,
                             "data": {
                               "key": "value"
                             }
                           }
                         }
                        """
                    )
                )
        );

        // when
        final var response = mailSenderRestClient.sendEmail();

        // then
        assertThat(response.data()).isNotNull();
        assertThat(response.data())
            .satisfies(sendingResult -> {
                assertThat(sendingResult.id()).isEqualTo(UUID.fromString("979f88cd-acfc-41f1-b5d9-e391363baec6"));
                assertThat(sendingResult.collectionId()).isEqualTo(UUID.fromString("48c4c730-7a2a-4dd0-a645-e0291b472938"));
            });

        wireMockServer.verify(
            getRequestedFor(urlEqualTo("/api/collections/send-email/records?project_id=23618"))
                .withHeader("x-api-testing-key", equalTo("test-api-key"))
        );
    }
}
