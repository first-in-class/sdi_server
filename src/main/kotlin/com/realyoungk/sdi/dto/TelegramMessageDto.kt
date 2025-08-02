package com.realyoungk.sdi.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TelegramMessageDto(
    @JsonProperty("message_id") val messageId: Long,
    @JsonProperty("chat") val chat: TelegramChatDto,
    @JsonProperty("text") val text: String
)