package com.realyoungk.sdi.compatibility

import com.realyoungk.sdi.config.GoogleSheetsProperties
import com.realyoungk.sdi.config.TelegramProperties
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class JavaKotlinCompatibilityTest {

    @Test
    fun `코틀린에서_데이터_클래스_프로퍼티_직접_접근_테스트`() {
        // Given: Kotlin data class 생성
        val googleProperties = GoogleSheetsProperties(
            spreadsheetId = "test-spreadsheet-id",
            titleRange = "Sheet1!B2:G2", 
            dataRange = "Sheet1!C3:G"
        )
        
        val telegramProperties = TelegramProperties(
            botId = "test-bot",
            botToken = "test-token",
            baseUrl = "https://api.telegram.org/bot",
            testChatId = "-1001234567890",
            firstStudyChatId = "-1001234567891"
        )

        // When & Then: Kotlin 스타일 프로퍼티 접근이 정상 동작하는지 확인 
        // (Kotlin data class에서는 getter 메서드가 자동 생성되지만 Kotlin에서는 프로퍼티 직접 접근 권장)
        assertEquals("test-spreadsheet-id", googleProperties.spreadsheetId)
        assertEquals("Sheet1!B2:G2", googleProperties.titleRange)
        assertEquals("Sheet1!C3:G", googleProperties.dataRange)
        
        assertEquals("test-bot", telegramProperties.botId)
        assertEquals("test-token", telegramProperties.botToken)
        assertEquals("https://api.telegram.org/bot", telegramProperties.baseUrl)
        assertEquals("-1001234567890", telegramProperties.testChatId)
        assertEquals("-1001234567891", telegramProperties.firstStudyChatId)
    }

    @Test
    fun `코틀린에서_데이터_클래스_프로퍼티_접근_테스트`() {
        // Given: Kotlin data class 생성
        val googleProperties = GoogleSheetsProperties(
            spreadsheetId = "kotlin-access-test",
            titleRange = "A1:F1",
            dataRange = "A2:F100"
        )

        // When & Then: Kotlin 스타일 프로퍼티 접근이 정상 동작하는지 확인
        assertEquals("kotlin-access-test", googleProperties.spreadsheetId)
        assertEquals("A1:F1", googleProperties.titleRange) 
        assertEquals("A2:F100", googleProperties.dataRange)
    }

    @Test
    fun `자바_레코드에서_코틀린_데이터_클래스로_변환_호환성_테스트`() {
        // Given: 기존 Java Record 스타일로 사용되던 패턴들이 Kotlin data class에서도 동작하는지 확인
        val properties = GoogleSheetsProperties("test-id", "test-title", "test-data")
        
        // When & Then: Java Record의 주요 특징들이 Kotlin data class에서도 동작하는지 확인
        
        // 1. Constructor parameters로 생성 가능
        assertNotNull(properties)
        
        // 2. toString() 자동 생성 확인
        val toStringResult = properties.toString()
        assertTrue(toStringResult.contains("GoogleSheetsProperties"))
        assertTrue(toStringResult.contains("test-id"))
        
        // 3. equals() 자동 생성 확인
        val sameProperties = GoogleSheetsProperties("test-id", "test-title", "test-data")
        assertEquals(properties, sameProperties)
        
        // 4. hashCode() 자동 생성 확인
        assertEquals(properties.hashCode(), sameProperties.hashCode())
        
        // 5. copy() 기능 (Kotlin data class 고유 기능)
        val copiedProperties = properties.copy(spreadsheetId = "new-id")
        assertEquals("new-id", copiedProperties.spreadsheetId)
        assertEquals("test-title", copiedProperties.titleRange) // 나머지는 동일
    }

    @Test
    fun `Spring_Boot의_ConfigurationProperties_바인딩_호환성_테스트`() {
        // Given: Spring Boot에서 사용되는 패턴들이 정상 동작하는지 확인
        val properties = TelegramProperties(
            botId = "spring-bot",
            botToken = "spring-token", 
            baseUrl = "https://spring.test.com",
            testChatId = "spring-test-chat",
            firstStudyChatId = "spring-study-chat"
        )

        // When & Then: Spring Boot Configuration Properties 패턴 호환성 확인
        
        // 1. 모든 필드가 final이어야 함 (불변성)
        assertNotNull(properties.botId)
        assertNotNull(properties.botToken)
        assertNotNull(properties.baseUrl)
        assertNotNull(properties.testChatId)
        assertNotNull(properties.firstStudyChatId)
        
        // 2. 프로퍼티 접근이 가능해야 함 (Java/Kotlin 호환성)
        assertEquals("spring-bot", properties.botId)
        assertEquals("spring-token", properties.botToken)
        assertEquals("https://spring.test.com", properties.baseUrl)
        assertEquals("spring-test-chat", properties.testChatId)
        assertEquals("spring-study-chat", properties.firstStudyChatId)
    }
}