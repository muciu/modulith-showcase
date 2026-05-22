package com.muciociosan.theproject.adapters.mailsenderclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GetUsersResponseDto(
        @JsonProperty("data") List<UserDataDto> data,
        @JsonProperty("page") Integer page,
        @JsonProperty("total") Integer total
) {
}
