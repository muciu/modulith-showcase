package com.muciociosan.theproject.adapters.mailsenderclient;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "theproject.clients.mailsender")
@Validated
public record MailSenderProperties(
    @NotNull URI url,
    @NotNull String apiKey,
    @NotNull String apiKeyHeaderName,
    @NotNull Duration requestTimeout,
    @NotNull Duration readTimeout,
    @NotNull Duration connectionTimeout
) {

    @AssertTrue(message = "theproject.clients.mailsender.request-timeout must be greater than 0")
    boolean isRequestTimeoutPositive() {
        return isPositive(requestTimeout);
    }

    @AssertTrue(message = "theproject.clients.mailsender.read-timeout must be greater than 0")
    boolean isReadTimeoutPositive() {
        return isPositive(readTimeout);
    }

    @AssertTrue(message = "theproject.clients.mailsender.connection-timeout must be greater than 0")
    boolean isConnectionTimeoutPositive() {
        return isPositive(connectionTimeout);
    }

    private static boolean isPositive(final Duration duration) {
        return duration != null && !duration.isNegative() && !duration.isZero();
    }
}
