package com.muciociosan.theproject.users.mailsenderclient;

import com.muciociosan.theproject.adapters.mailsenderclient.MailSenderProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MailSenderProperties")
class MailSenderPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withUserConfiguration(MailSenderPropertiesConfiguration.class);

    @Nested
    @DisplayName("startup validation")
    class StartupValidation {

        @Test
        @DisplayName("should fail startup when timeout cannot be bound to duration")
        void shouldFailStartupWhenTimeoutCannotBeBoundToDuration() {
            // given
            final ApplicationContextRunner runner = contextRunner.withPropertyValues(
                "theproject.clients.mailsender.url=https://reqres.in",
                "theproject.clients.mailsender.api-key=test-key",
                "theproject.clients.mailsender.api-key-header-name=x-api-key",
                "theproject.clients.mailsender.request-timeout=xyz",
                "theproject.clients.mailsender.read-timeout=10s",
                "theproject.clients.mailsender.connection-timeout=5s"
            );

            // when

            // then
            runner.run(context -> {
                assertThat(context).hasFailed();
                final Throwable failure = context.getStartupFailure();
                assertThat(failure)
                    .hasStackTraceContaining("theproject.clients.mailsender.request-timeout")
                    .hasStackTraceContaining("java.time.Duration");
                assertTrue(hasCauseMessageContaining(failure, "value [xyz]"));
            });
        }

        @Test
        @DisplayName("should fail startup when configured durations are not positive")
        void shouldFailStartupWhenConfiguredDurationsAreNotPositive() {
            // given
            final ApplicationContextRunner runner = contextRunner.withPropertyValues(
                "theproject.clients.mailsender.url=https://reqres.in",
                "theproject.clients.mailsender.api-key=test-key",
                "theproject.clients.mailsender.api-key-header-name=x-api-key",
                "theproject.clients.mailsender.request-timeout=0s",
                "theproject.clients.mailsender.read-timeout=-1s",
                "theproject.clients.mailsender.connection-timeout=5s"
            );

            // when

            // then
            runner.run(context -> {
                assertThat(context).hasFailed();
                final Throwable failure = context.getStartupFailure();
                assertTrue(
                    hasCauseMessageContaining(
                        failure,
                        "theproject.clients.mailsender.request-timeout must be greater than 0"
                    )
                );
            });
        }

        @Test
        @DisplayName("should create properties bean when values are valid")
        void shouldCreatePropertiesBeanWhenValuesAreValid() {
            // given
            final ApplicationContextRunner runner = contextRunner.withPropertyValues(
                "theproject.clients.mailsender.url=https://reqres.in",
                "theproject.clients.mailsender.api-key=test-key",
                "theproject.clients.mailsender.api-key-header-name=x-api-key",
                "theproject.clients.mailsender.request-timeout=5s",
                "theproject.clients.mailsender.read-timeout=10s",
                "theproject.clients.mailsender.connection-timeout=5s"
            );

            // when

            // then
            runner.run(context -> {
                assertThat(context).hasNotFailed();
                assertThat(context).hasSingleBean(MailSenderProperties.class);

                final MailSenderProperties properties = context.getBean(MailSenderProperties.class);
                assertAll(
                    () -> assertTrue(properties.url().isAbsolute()),
                    () -> assertTrue(properties.requestTimeout().toSeconds() > 0),
                    () -> assertTrue(properties.readTimeout().toSeconds() > 0),
                    () -> assertTrue(properties.connectionTimeout().toSeconds() > 0)
                );
            });
        }
    }

    @Configuration
    @EnableConfigurationProperties(MailSenderProperties.class)
    static class MailSenderPropertiesConfiguration {
    }

    private static boolean hasCauseMessageContaining(final Throwable throwable, final String text) {
        Throwable current = throwable;
        while (current != null) {
            if (current.getMessage() != null && current.getMessage().contains(text)) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
