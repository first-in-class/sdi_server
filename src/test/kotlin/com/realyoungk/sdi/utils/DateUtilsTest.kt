package com.realyoungk.sdi.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate

class DateUtilsTest {

    // formatToKoreanMonthDay 테스트
    @Test
    fun `날짜를_한국어_월일_형식으로_포맷팅해야한다`() {
        // Given
        val date = LocalDate.of(2023, 12, 25)
        
        // When
        val result = DateUtils.formatToKoreanMonthDay(date)
        
        // Then
        assertEquals("12월 25일", result)
    }

    @Test
    fun `null_날짜는_기본_메시지를_반환해야한다`() {
        // When
        val result = DateUtils.formatToKoreanMonthDay(null)
        
        // Then
        assertEquals("정보 없음", result)
    }

    @Test
    fun `null_날짜에_대해_커스텀_메시지를_반환할_수_있어야한다`() {
        // When
        val result = DateUtils.formatToKoreanMonthDay(null, "날짜 미정")
        
        // Then
        assertEquals("날짜 미정", result)
    }

    @Test
    fun `한자리수_월과_일을_앞에_0을_붙여_포맷팅해야한다`() {
        // Given
        val date = LocalDate.of(2023, 1, 5)
        
        // When
        val result = DateUtils.formatToKoreanMonthDay(date)
        
        // Then
        assertEquals("01월 05일", result)
    }

    // isDateInValidRange 테스트
    @Test
    fun `null_날짜는_유효하지않다고_판단해야한다`() {
        // When
        val result = DateUtils.isDateInValidRange(null)
        
        // Then
        assertFalse(result)
    }

    @Test
    fun `일반적인_과거_날짜는_유효하다고_판단해야한다`() {
        // Given
        val pastDate = LocalDate.now().minusYears(25)
        
        // When
        val result = DateUtils.isDateInValidRange(pastDate)
        
        // Then
        assertTrue(result)
    }

    @Test
    fun `미래_날짜는_기본적으로_유효하지않다고_판단해야한다`() {
        // Given
        val futureDate = LocalDate.now().plusDays(1)
        
        // When
        val result = DateUtils.isDateInValidRange(futureDate)
        
        // Then
        assertFalse(result)
    }

    @Test
    fun `기본_150년보다_오래된_날짜는_유효하지않다고_판단해야한다`() {
        // Given
        val tooOldDate = LocalDate.now().minusYears(151)
        
        // When
        val result = DateUtils.isDateInValidRange(tooOldDate)
        
        // Then
        assertFalse(result)
    }

    @Test
    fun `커스텀_연수_제한을_설정할_수_있어야한다`() {
        // Given
        val date = LocalDate.now().minusYears(200)
        
        // When
        val resultWith150 = DateUtils.isDateInValidRange(date, maxYearsAgo = 150)
        val resultWith250 = DateUtils.isDateInValidRange(date, maxYearsAgo = 250)
        
        // Then
        assertFalse(resultWith150)
        assertTrue(resultWith250)
    }

    @Test
    fun `오늘_날짜_허용_옵션을_사용할_수_있어야한다`() {
        // Given
        val today = LocalDate.now()
        
        // When
        val resultNotAllowed = DateUtils.isDateInValidRange(today, allowToday = false)
        val resultAllowed = DateUtils.isDateInValidRange(today, allowToday = true)
        
        // Then
        assertFalse(resultNotAllowed)
        assertTrue(resultAllowed)
    }

    // isPastDate 테스트
    @Test
    fun `과거_날짜를_올바르게_판단해야한다`() {
        // Given
        val pastDate = LocalDate.now().minusDays(1)
        
        // When
        val result = DateUtils.isPastDate(pastDate)
        
        // Then
        assertTrue(result)
    }

    @Test
    fun `오늘_날짜는_기본적으로_과거가_아니라고_판단해야한다`() {
        // Given
        val today = LocalDate.now()
        
        // When
        val result = DateUtils.isPastDate(today)
        
        // Then
        assertFalse(result)
    }

    @Test
    fun `오늘_포함_옵션으로_오늘을_과거로_판단할_수_있어야한다`() {
        // Given
        val today = LocalDate.now()
        
        // When
        val result = DateUtils.isPastDate(today, includeToday = true)
        
        // Then
        assertTrue(result)
    }

    @Test
    fun `미래_날짜는_과거가_아니라고_판단해야한다`() {
        // Given
        val futureDate = LocalDate.now().plusDays(1)
        
        // When
        val result = DateUtils.isPastDate(futureDate)
        
        // Then
        assertFalse(result)
    }
}