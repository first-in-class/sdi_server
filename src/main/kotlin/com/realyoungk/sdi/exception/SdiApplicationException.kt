package com.realyoungk.sdi.exception

import org.springframework.http.HttpStatus

/**
 * 에러 타입별 HTTP 상태 코드와 에러 코드를 정의합니다.
 */
enum class ErrorType(val httpStatus: HttpStatus, val errorCode: String) {
    // 4xx 클라이언트 에러
    NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "INVALID_INPUT"),
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
    PERMISSION_ERROR(HttpStatus.FORBIDDEN, "ACCESS_DENIED"),
    
    // 5xx 서버 에러
    EXTERNAL_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR")
}

/**
 * SDI 애플리케이션의 모든 커스텀 런타임 예외의 최상위 부모 클래스입니다.
 */
abstract class SdiApplicationException(
    message: String,
    cause: Throwable? = null,
    val errorType: ErrorType,
    val details: Map<String, Any> = emptyMap()
) : RuntimeException(message, cause)

/**
 * 도메인 레이어 (비즈니스 로직) 예외들
 */
sealed class DomainException(
    message: String,
    errorType: ErrorType,
    details: Map<String, Any> = emptyMap()
) : SdiApplicationException(message, null, errorType, details) {

    class UserNotFound(userId: String) : DomainException(
        message = "사용자를 찾을 수 없습니다.",
        errorType = ErrorType.NOT_FOUND,
        details = mapOf("userId" to userId)
    )

    class VisitNotFound(visitId: String) : DomainException(
        message = "방문 일정을 찾을 수 없습니다.",
        errorType = ErrorType.NOT_FOUND,
        details = mapOf("visitId" to visitId)
    )

    class InvalidBirthday(reason: String) : DomainException(
        message = "생일 정보가 올바르지 않습니다.",
        errorType = ErrorType.VALIDATION_ERROR,
        details = mapOf("reason" to reason)
    )
}

/**
 * 인프라 레이어 (외부 서비스) 예외들
 */
sealed class InfrastructureException(
    message: String,
    cause: Throwable,
    errorType: ErrorType,
    details: Map<String, Any> = emptyMap()
) : SdiApplicationException(message, cause, errorType, details) {

    class GoogleSheetsUnavailable(cause: Throwable) : InfrastructureException(
        message = "방문 일정을 가져올 수 없습니다. 잠시 후 다시 시도해주세요.",
        cause = cause,
        errorType = ErrorType.EXTERNAL_SERVICE_ERROR,
        details = mapOf("service" to "GoogleSheets")
    )

    class DatabaseConnectionFailed(cause: Throwable) : InfrastructureException(
        message = "데이터베이스 연결에 실패했습니다.",
        cause = cause,
        errorType = ErrorType.DATABASE_ERROR
    )

    class TelegramApiError(cause: Throwable) : InfrastructureException(
        message = "텔레그램 메시지 전송에 실패했습니다.",
        cause = cause,
        errorType = ErrorType.EXTERNAL_SERVICE_ERROR,
        details = mapOf("service" to "Telegram")
    )
}

/**
 * 입력 검증 예외들
 */
sealed class ValidationException(
    message: String,
    details: Map<String, Any> = emptyMap()
) : SdiApplicationException(message, null, ErrorType.VALIDATION_ERROR, details) {

    class MissingRequiredField(field: String) : ValidationException(
        message = "필수 입력값이 누락되었습니다.",
        details = mapOf("field" to field)
    )

    class InvalidFormat(field: String, expected: String) : ValidationException(
        message = "입력 형식이 올바르지 않습니다.",
        details = mapOf("field" to field, "expected" to expected)
    )

    class OutOfRange(field: String, min: Any, max: Any) : ValidationException(
        message = "입력값이 허용 범위를 벗어났습니다.",
        details = mapOf("field" to field, "min" to min, "max" to max)
    )
}

