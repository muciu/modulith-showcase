package com.muciociosan.theproject.adapters.mailsenderclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MailSendingClientResponseDto(
    @JsonProperty("status") String status
) {
}
