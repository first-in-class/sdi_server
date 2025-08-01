package com.realyoungk.sdi.enums

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class CalendarTypeTest {

    @Test
    fun `양력_타입은_올바른_한글_설명을_가져야한다`() {
        // When
        val result = CalendarType.SOLAR.description
        
        // Then
        assertEquals("양력", result)
    }

    @Test
    fun `음력_타입은_올바른_한글_설명을_가져야한다`() {
        // When
        val result = CalendarType.LUNAR.description
        
        // Then
        assertEquals("음력", result)
    }

    @Test
    fun `달력_타입은_정확히_2개여야한다`() {
        // When
        val values = CalendarType.values()
        
        // Then
        assertEquals(2, values.size)
        assertTrue(values.contains(CalendarType.SOLAR))
        assertTrue(values.contains(CalendarType.LUNAR))
    }

    @Test
    fun `문자열로부터_달력_타입을_올바르게_변환할_수_있어야한다`() {
        // When & Then
        assertEquals(CalendarType.SOLAR, CalendarType.valueOf("SOLAR"))
        assertEquals(CalendarType.LUNAR, CalendarType.valueOf("LUNAR"))
    }
}