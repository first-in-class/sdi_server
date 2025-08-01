package com.realyoungk.sdi.enums

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class LunarMonthTypeTest {

    @Test
    fun `일반달_타입은_올바른_한글_설명을_가져야한다`() {
        // When
        val result = LunarMonthType.NORMAL.description
        
        // Then
        assertEquals("일반달", result)
    }

    @Test
    fun `윤달_타입은_올바른_한글_설명을_가져야한다`() {
        // When
        val result = LunarMonthType.LEAP.description
        
        // Then
        assertEquals("윤달", result)
    }

    @Test
    fun `음력_달_타입은_정확히_2개여야한다`() {
        // When
        val values = LunarMonthType.values()
        
        // Then
        assertEquals(2, values.size)
        assertTrue(values.contains(LunarMonthType.NORMAL))
        assertTrue(values.contains(LunarMonthType.LEAP))
    }

    @Test
    fun `문자열로부터_음력_달_타입을_올바르게_변환할_수_있어야한다`() {
        // When & Then
        assertEquals(LunarMonthType.NORMAL, LunarMonthType.valueOf("NORMAL"))
        assertEquals(LunarMonthType.LEAP, LunarMonthType.valueOf("LEAP"))
    }

    @Test
    fun `윤달은_음력_달력에서_특별한_의미를_가진_달을_나타낸다`() {
        // Given
        val leapMonth = LunarMonthType.LEAP
        val normalMonth = LunarMonthType.NORMAL
        
        // Then
        assertNotEquals(leapMonth, normalMonth)
        assertEquals("윤달", leapMonth.description)
        assertEquals("일반달", normalMonth.description)
    }
}