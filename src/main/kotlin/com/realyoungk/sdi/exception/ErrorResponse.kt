package com.realyoungk.sdi.exception

import java.time.Instant

/**
 * API 에러 응답을 위한 표준 형식
 */
data class ErrorResponse(
    val code: String,
    val message: String,
    val timestamp: Instant = Instant.now(),
    val path: String? = null,
    val details: Map<String, Any>? = null
)