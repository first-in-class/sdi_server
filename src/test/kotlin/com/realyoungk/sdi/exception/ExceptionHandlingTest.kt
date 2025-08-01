package com.realyoungk.sdi.exception

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.http.HttpStatus
import java.io.IOException

class ExceptionHandlingTest {

    @Test
    fun `도메인_예외_생성_테스트`() {
        // Given
        val userId = "user123"
        
        // When
        val exception = DomainException.UserNotFound(userId)
        
        // Then
        assertEquals("사용자를 찾을 수 없습니다.", exception.message)
        assertEquals(ErrorType.NOT_FOUND, exception.errorType)
        assertEquals(HttpStatus.NOT_FOUND, exception.errorType.httpStatus)
        assertEquals("RESOURCE_NOT_FOUND", exception.errorType.errorCode)
        assertEquals(userId, exception.details["userId"])
    }

    @Test
    fun `인프라_예외_생성_테스트`() {
        // Given
        val ioException = IOException("연결 실패")
        
        // When
        val exception = InfrastructureException.GoogleSheetsUnavailable(ioException)
        
        // Then
        assertEquals("방문 일정을 가져올 수 없습니다. 잠시 후 다시 시도해주세요.", exception.message)
        assertEquals(ErrorType.EXTERNAL_SERVICE_ERROR, exception.errorType)
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.errorType.httpStatus)
        assertEquals("SERVICE_UNAVAILABLE", exception.errorType.errorCode)
        assertEquals("GoogleSheets", exception.details["service"])
        assertEquals(ioException, exception.cause)
    }

    @Test
    fun `검증_예외_생성_테스트`() {
        // Given
        val field = "email"
        val expected = "이메일 형식"
        
        // When
        val exception = ValidationException.InvalidFormat(field, expected)
        
        // Then
        assertEquals("입력 형식이 올바르지 않습니다.", exception.message)
        assertEquals(ErrorType.VALIDATION_ERROR, exception.errorType)
        assertEquals(HttpStatus.BAD_REQUEST, exception.errorType.httpStatus)
        assertEquals("INVALID_INPUT", exception.errorType.errorCode)
        assertEquals(field, exception.details["field"])
        assertEquals(expected, exception.details["expected"])
    }

    @Test
    fun `범위_초과_검증_예외_테스트`() {
        // Given
        val field = "age"
        val min = 0
        val max = 120
        
        // When
        val exception = ValidationException.OutOfRange(field, min, max)
        
        // Then
        assertEquals("입력값이 허용 범위를 벗어났습니다.", exception.message)
        assertEquals(field, exception.details["field"])
        assertEquals(min, exception.details["min"])
        assertEquals(max, exception.details["max"])
    }

    @Test
    fun `필수_필드_누락_예외_테스트`() {
        // Given
        val field = "name"
        
        // When
        val exception = ValidationException.MissingRequiredField(field)
        
        // Then
        assertEquals("필수 입력값이 누락되었습니다.", exception.message)
        assertEquals(field, exception.details["field"])
    }

    @Test
    fun `텔레그램_API_에러_테스트`() {
        // Given
        val apiException = RuntimeException("API 호출 실패")
        
        // When
        val exception = InfrastructureException.TelegramApiError(apiException)
        
        // Then
        assertEquals("텔레그램 메시지 전송에 실패했습니다.", exception.message)
        assertEquals(ErrorType.EXTERNAL_SERVICE_ERROR, exception.errorType)
        assertEquals("Telegram", exception.details["service"])
        assertEquals(apiException, exception.cause)
    }

    @Test
    fun `데이터베이스_연결_실패_테스트`() {
        // Given
        val dbException = RuntimeException("DB 연결 실패")
        
        // When
        val exception = InfrastructureException.DatabaseConnectionFailed(dbException)
        
        // Then
        assertEquals("데이터베이스 연결에 실패했습니다.", exception.message)
        assertEquals(ErrorType.DATABASE_ERROR, exception.errorType)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.errorType.httpStatus)
        assertEquals(dbException, exception.cause)
    }

    @Test
    fun `생일_정보_오류_테스트`() {
        // Given
        val reason = "미래 날짜는 생일로 설정할 수 없습니다"
        
        // When
        val exception = DomainException.InvalidBirthday(reason)
        
        // Then
        assertEquals("생일 정보가 올바르지 않습니다.", exception.message)
        assertEquals(ErrorType.VALIDATION_ERROR, exception.errorType)
        assertEquals(reason, exception.details["reason"])
    }

    @Test
    fun `방문_일정_없음_테스트`() {
        // Given
        val visitId = "visit456"
        
        // When
        val exception = DomainException.VisitNotFound(visitId)
        
        // Then
        assertEquals("방문 일정을 찾을 수 없습니다.", exception.message)
        assertEquals(ErrorType.NOT_FOUND, exception.errorType)
        assertEquals(visitId, exception.details["visitId"])
    }
}