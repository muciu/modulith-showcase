package com.muciociosan.theproject.adapters.mailsenderclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MailSendingClientResponseDto(
    @JsonProperty("data") MailSendingClientDto data
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MailSendingClientDto(
        @JsonProperty("id") UUID id,
        @JsonProperty("collection_id") UUID collectionId
    ) {
    }
}
