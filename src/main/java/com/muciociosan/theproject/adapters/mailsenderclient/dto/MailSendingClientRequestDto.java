package com.muciociosan.theproject.adapters.mailsenderclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MailSendingClientRequestDto(
    @JsonProperty("from") String from,
    @JsonProperty("to") String to,
    @JsonProperty("title") String title,
    @JsonProperty("content") String content
) {
}
