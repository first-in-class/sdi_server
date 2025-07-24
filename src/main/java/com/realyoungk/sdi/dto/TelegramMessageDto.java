package com.realyoungk.sdi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TelegramMessageDto(
        @JsonProperty("message_id") Long messageId,
        @JsonProperty("chat") TelegramChatDto chat,
        @JsonProperty("text") String text
) {
}
