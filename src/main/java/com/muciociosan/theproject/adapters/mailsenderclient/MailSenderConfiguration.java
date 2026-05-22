package com.muciociosan.theproject.adapters.mailsenderclient;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.http.HttpClient;

@Configuration
@EnableConfigurationProperties(MailSenderProperties.class)
@ConditionalOnProperty(prefix = "theproject.clients.mailsender", name = "url")
@RequiredArgsConstructor
class MailSenderConfiguration {

    private final MailSenderProperties properties;

    @Bean
    MailSenderRestClient senderClient(final RestClient.Builder restClientBuilder) {
        final var requestFactory = new JdkClientHttpRequestFactory(HttpClient.newBuilder()
                                        .connectTimeout(properties.connectionTimeout())
                                        .build());
        requestFactory.setReadTimeout(properties.readTimeout());

        final var restClient = restClientBuilder
                .baseUrl(properties.url())
                .requestFactory(requestFactory)
                .requestInterceptor(requestInterceptor())
                .build();
        final var clientFactory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return clientFactory.createClient(MailSenderRestClient.class);
    }

    ClientHttpRequestInterceptor requestInterceptor() {
        return (request, body, execution) -> {
            request.getHeaders().add(properties.apiKeyHeaderName(), properties.apiKey());
            return execution.execute(request, body);
        };
    }
}
