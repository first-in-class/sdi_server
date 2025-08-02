package com.realyoungk.sdi.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

class VisitModelTest {

    @Test
    fun `data class 기본 기능 테스트`() {
        // Given
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDate = dateFormat.parse("2025-03-15")
        
        val visitModel = VisitModel(
            id = "visit-001",
            companyName = "테스트 회사",
            startedAt = startDate,
            finishedAt = null,
            participantCount = "5명",
            teamName = "1기",
            organizer = "홍길동",
            remark = "중요한 탐방"
        )
        
        // When & Then
        assertEquals("visit-001", visitModel.id)
        assertEquals("테스트 회사", visitModel.companyName)
        assertEquals(startDate, visitModel.startedAt)
        assertEquals("5명", visitModel.participantCount)
        assertEquals("1기", visitModel.teamName)
        assertEquals("홍길동", visitModel.organizer)
        assertEquals("중요한 탐방", visitModel.remark)
    }

    @Test
    fun `toDetailedString 메서드 테스트`() {
        // Given
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDate = dateFormat.parse("2025-03-15")
        
        val visitModel = VisitModel(
            id = "visit-001",
            companyName = "테스트 회사",
            startedAt = startDate,
            finishedAt = null,
            participantCount = "5명",
            teamName = "1기",
            organizer = "홍길동",
            remark = "중요한 탐방"
        )
        
        // When
        val result = visitModel.toDetailedString(7)
        
        // Then
        assertTrue(result.contains("‼️ *D-7* ‼️"))
        assertTrue(result.contains("*회사명*: 테스트 회사"))
        assertTrue(result.contains("*기수*: 1기"))
        assertTrue(result.contains("*주선자*: 홍길동"))
        assertTrue(result.contains("*참여 인원*: 5명"))
        assertTrue(result.contains("*비고*: 중요한 탐방"))
    }

    @Test
    fun `toDetailedString 비고가 null일 때 테스트`() {
        // Given
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDate = dateFormat.parse("2025-03-15")
        
        val visitModel = VisitModel(
            id = "visit-001",
            companyName = "테스트 회사",
            startedAt = startDate,
            finishedAt = null,
            participantCount = "5명",
            teamName = "1기",
            organizer = "홍길동",
            remark = null
        )
        
        // When
        val result = visitModel.toDetailedString(3)
        
        // Then
        assertTrue(result.contains("‼️ *D-3* ‼️"))
        assertTrue(result.contains("*회사명*: 테스트 회사"))
        assertFalse(result.contains("*비고*:"))
    }

    @Test
    fun `toDetailedString 비고가 빈 문자열일 때 테스트`() {
        // Given
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDate = dateFormat.parse("2025-03-15")
        
        val visitModel = VisitModel(
            id = "visit-001",
            companyName = "테스트 회사",
            startedAt = startDate,
            finishedAt = null,
            participantCount = "5명",
            teamName = "1기",
            organizer = "홍길동",
            remark = "   "  // 공백만 있는 문자열
        )
        
        // When
        val result = visitModel.toDetailedString(1)
        
        // Then
        assertTrue(result.contains("‼️ *D-1* ‼️"))
        assertFalse(result.contains("*비고*:"))
    }

    @Test
    fun `toSimpleString 메서드 테스트`() {
        // Given
        val visitModel = VisitModel(
            id = "visit-001",
            companyName = "테스트 회사",
            startedAt = null,
            finishedAt = null,
            participantCount = "5명",
            teamName = "1기",
            organizer = "홍길동",
            remark = null
        )
        
        // When
        val result = visitModel.toSimpleString(10)
        
        // Then
        assertEquals("🗓️ D-10 / 테스트 회사 / 1기 홍길동\n", result)
    }

    @Test
    fun `기본값 생성자 테스트`() {
        // Given & When
        val visitModel = VisitModel()
        
        // Then
        assertNull(visitModel.id)
        assertNull(visitModel.companyName)
        assertNull(visitModel.startedAt)
        assertNull(visitModel.finishedAt)
        assertNull(visitModel.participantCount)
        assertNull(visitModel.teamName)
        assertNull(visitModel.organizer)
        assertNull(visitModel.remark)
    }

    @Test
    fun `copy 메서드 테스트`() {
        // Given
        val original = VisitModel(
            id = "visit-001",
            companyName = "원본 회사",
            teamName = "1기",
            organizer = "홍길동"
        )
        
        // When
        val copied = original.copy(companyName = "수정된 회사")
        
        // Then
        assertEquals("visit-001", copied.id)
        assertEquals("수정된 회사", copied.companyName)
        assertEquals("1기", copied.teamName)
        assertEquals("홍길동", copied.organizer)
        
        // 원본은 변경되지 않음
        assertEquals("원본 회사", original.companyName)
    }

    @Test
    fun `equals와 hashCode 테스트`() {
        // Given
        val visit1 = VisitModel(
            id = "visit-001",
            companyName = "테스트 회사",
            teamName = "1기"
        )
        
        val visit2 = VisitModel(
            id = "visit-001",
            companyName = "테스트 회사",
            teamName = "1기"
        )
        
        val visit3 = VisitModel(
            id = "visit-002",
            companyName = "테스트 회사",
            teamName = "1기"
        )
        
        // When & Then
        assertEquals(visit1, visit2)
        assertNotEquals(visit1, visit3)
        assertEquals(visit1.hashCode(), visit2.hashCode())
    }
}