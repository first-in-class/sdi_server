package com.realyoungk.sdi.exception

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

/**
 * 전역 예외 처리기
 * 모든 컨트롤러에서 발생하는 예외를 중앙집중식으로 처리합니다.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * 도메인 예외 처리
     */
    @ExceptionHandler(DomainException::class)
    fun handleDomainException(
        ex: DomainException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("도메인 예외 발생: {}", ex.message, ex)
        
        val errorResponse = ErrorResponse(
            code = ex.errorType.errorCode,
            message = ex.message ?: "도메인 오류가 발생했습니다.",
            path = getRequestPath(request),
            details = ex.details.takeIf { it.isNotEmpty() }
        )
        
        return ResponseEntity.status(ex.errorType.httpStatus).body(errorResponse)
    }

    /**
     * 인프라스트럭처 예외 처리
     */
    @ExceptionHandler(InfrastructureException::class)
    fun handleInfrastructureException(
        ex: InfrastructureException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("인프라 예외 발생: {}", ex.message, ex)
        
        val errorResponse = ErrorResponse(
            code = ex.errorType.errorCode,
            message = ex.message ?: "외부 서비스 오류가 발생했습니다.",
            path = getRequestPath(request),
            details = ex.details.takeIf { it.isNotEmpty() }
        )
        
        return ResponseEntity.status(ex.errorType.httpStatus).body(errorResponse)
    }

    /**
     * 입력 검증 예외 처리
     */
    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(
        ex: ValidationException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("입력 검증 예외 발생: {}", ex.message, ex)
        
        val errorResponse = ErrorResponse(
            code = ex.errorType.errorCode,
            message = ex.message ?: "입력값이 올바르지 않습니다.",
            path = getRequestPath(request),
            details = ex.details.takeIf { it.isNotEmpty() }
        )
        
        return ResponseEntity.status(ex.errorType.httpStatus).body(errorResponse)
    }

    /**
     * 기타 SDI 애플리케이션 예외 처리
     */
    @ExceptionHandler(SdiApplicationException::class)
    fun handleSdiApplicationException(
        ex: SdiApplicationException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("애플리케이션 예외 발생: {}", ex.message, ex)
        
        val errorResponse = ErrorResponse(
            code = ex.errorType.errorCode,
            message = ex.message ?: "애플리케이션 오류가 발생했습니다.",
            path = getRequestPath(request),
            details = ex.details.takeIf { it.isNotEmpty() }
        )
        
        return ResponseEntity.status(ex.errorType.httpStatus).body(errorResponse)
    }

    /**
     * 예상치 못한 일반 예외 처리
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("예상치 못한 예외 발생: {}", ex.message, ex)
        
        val errorResponse = ErrorResponse(
            code = "INTERNAL_ERROR",
            message = "서버 내부 오류가 발생했습니다.",
            path = getRequestPath(request)
        )
        
        return ResponseEntity.internalServerError().body(errorResponse)
    }

    private fun getRequestPath(request: WebRequest): String? {
        return request.getDescription(false).removePrefix("uri=")
    }
}