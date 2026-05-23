package com.muciociosan.theproject.users.mailsenderclient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.muciociosan.theproject.adapters.mailsenderclient.MailSenderRestClient;
import com.muciociosan.theproject.adapters.mailsenderclient.dto.MailSendingClientRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
            post(urlEqualTo("/fake/mail-sender"))
                .withHeader("x-api-testing-key", equalTo("test-api-key"))
                .willReturn(
                    okJson(
                        """
                         {
                           "status": "OK"
                         }
                        """
                    )
                )
        );

        // when
        final var response = mailSenderRestClient.sendEmail(
                new MailSendingClientRequestDto("from@example", "to@example.com", "title", "content"));

        // then
        assertThat(response.status()).isEqualTo("OK");

        wireMockServer.verify(
            getRequestedFor(urlEqualTo("/fake/mail-sender"))
                .withHeader("x-api-testing-key", equalTo("test-api-key"))
        );
    }
}
