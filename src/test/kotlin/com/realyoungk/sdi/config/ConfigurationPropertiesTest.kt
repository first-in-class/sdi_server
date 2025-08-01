package com.realyoungk.sdi.config

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ConfigurationPropertiesTest {

    @Test
    fun `GoogleSheetsProperties_데이터_클래스_생성_테스트`() {
        // Given
        val spreadsheetId = "1BvHMwskDGS0Gc6ZgO3gK0xh1EYozZ3HUcByF2k"
        val titleRange = "Sheet1!B2:G2"
        val dataRange = "Sheet1!C3:G"
        
        // When
        val properties = GoogleSheetsProperties(
            spreadsheetId = spreadsheetId,
            titleRange = titleRange,
            dataRange = dataRange
        )
        
        // Then
        assertEquals(spreadsheetId, properties.spreadsheetId)
        assertEquals(titleRange, properties.titleRange)
        assertEquals(dataRange, properties.dataRange)
    }

    @Test
    fun `TelegramProperties_데이터_클래스_생성_테스트`() {
        // Given
        val botId = "testBot"
        val botToken = "123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11"
        val baseUrl = "https://api.telegram.org/bot"
        val testChatId = "-1001234567890"
        val firstStudyChatId = "-1001234567891"
        
        // When
        val properties = TelegramProperties(
            botId = botId,
            botToken = botToken,
            baseUrl = baseUrl,
            testChatId = testChatId,
            firstStudyChatId = firstStudyChatId
        )
        
        // Then
        assertEquals(botId, properties.botId)
        assertEquals(botToken, properties.botToken)
        assertEquals(baseUrl, properties.baseUrl)
        assertEquals(testChatId, properties.testChatId)
        assertEquals(firstStudyChatId, properties.firstStudyChatId)
    }

    @Test
    fun `GoogleSheetsProperties_불변성_테스트`() {
        // Given
        val properties = GoogleSheetsProperties(
            spreadsheetId = "test-id",
            titleRange = "A1:B1",
            dataRange = "A2:B10"
        )
        
        // When & Then
        // val 키워드로 인해 프로퍼티 변경 불가능
        // properties.spreadsheetId = "new-id" // 컴파일 에러
        
        // 하지만 읽기는 가능
        assertNotNull(properties.spreadsheetId)
        assertNotNull(properties.titleRange)
        assertNotNull(properties.dataRange)
    }

    @Test
    fun `TelegramProperties_불변성_테스트`() {
        // Given
        val properties = TelegramProperties(
            botId = "test",
            botToken = "token",
            baseUrl = "https://test.com",
            testChatId = "chat1",
            firstStudyChatId = "chat2"
        )
        
        // When & Then
        // val 키워드로 인해 프로퍼티 변경 불가능
        // properties.botId = "new-bot" // 컴파일 에러
        
        // 하지만 읽기는 가능
        assertNotNull(properties.botId)
        assertNotNull(properties.botToken)
        assertNotNull(properties.baseUrl)
        assertNotNull(properties.testChatId)
        assertNotNull(properties.firstStudyChatId)
    }

    @Test
    fun `데이터_클래스_equals_및_hashCode_테스트`() {
        // Given
        val properties1 = GoogleSheetsProperties("id1", "range1", "range2")
        val properties2 = GoogleSheetsProperties("id1", "range1", "range2")
        val properties3 = GoogleSheetsProperties("id2", "range1", "range2")
        
        // When & Then
        assertEquals(properties1, properties2)
        assertNotEquals(properties1, properties3)
        assertEquals(properties1.hashCode(), properties2.hashCode())
        assertNotEquals(properties1.hashCode(), properties3.hashCode())
    }

    @Test
    fun `데이터_클래스_toString_테스트`() {
        // Given
        val properties = GoogleSheetsProperties("test-id", "A1:B1", "A2:B10")
        
        // When
        val toString = properties.toString()
        
        // Then
        assertTrue(toString.contains("GoogleSheetsProperties"))
        assertTrue(toString.contains("test-id"))
        assertTrue(toString.contains("A1:B1"))
        assertTrue(toString.contains("A2:B10"))
    }

    @Test
    fun `데이터_클래스_copy_기능_테스트`() {
        // Given
        val original = TelegramProperties(
            botId = "bot1",
            botToken = "token1",
            baseUrl = "url1",
            testChatId = "chat1",
            firstStudyChatId = "study1"
        )
        
        // When
        val copied = original.copy(botId = "bot2")
        
        // Then
        assertEquals("bot2", copied.botId)
        assertEquals("token1", copied.botToken) // 나머지는 그대로
        assertEquals("url1", copied.baseUrl)
        assertEquals("chat1", copied.testChatId)
        assertEquals("study1", copied.firstStudyChatId)
        assertNotEquals(original, copied)
    }
}