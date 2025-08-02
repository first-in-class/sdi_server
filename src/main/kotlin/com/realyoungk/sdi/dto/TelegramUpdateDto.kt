package com.realyoungk.sdi.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TelegramUpdateDto(
    @JsonProperty("update_id") val updateId: Long,
    @JsonProperty("message") val message: TelegramMessageDto
)