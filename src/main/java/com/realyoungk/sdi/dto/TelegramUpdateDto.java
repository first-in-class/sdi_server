package com.realyoungk.sdi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TelegramUpdateDto(
        @JsonProperty("update_id") Long updateId,
        @JsonProperty("message") TelegramMessageDto message
) {
}
