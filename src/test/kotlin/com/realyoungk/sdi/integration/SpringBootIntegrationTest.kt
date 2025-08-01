package com.realyoungk.sdi.integration

import com.realyoungk.sdi.config.GoogleSheetsProperties
import com.realyoungk.sdi.config.TelegramProperties
import org.junit.jupiter.api.Test
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.context.TestPropertySource
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Assertions.*

@SpringJUnitConfig(initializers = [ConfigDataApplicationContextInitializer::class])
@EnableConfigurationProperties(GoogleSheetsProperties::class, TelegramProperties::class)
@TestPropertySource(properties = [
    "google.sheets.spreadsheet-id=test-spreadsheet-id",
    "google.sheets.title-range=Sheet1!B2:G2", 
    "google.sheets.data-range=Sheet1!C3:G",
    "telegram.bot-id=test-bot",
    "telegram.bot-token=test-token",
    "telegram.base-url=https://api.telegram.org/bot",
    "telegram.test-chat-id=-1001234567890",
    "telegram.first-study-chat-id=-1001234567891"
])
class SpringBootIntegrationTest {

    @Autowired
    private lateinit var googleSheetsProperties: GoogleSheetsProperties
    
    @Autowired 
    private lateinit var telegramProperties: TelegramProperties

    @Test
    fun `스프링_부트_컨텍스트에서_GoogleSheetsProperties_자동_바인딩_테스트`() {
        // When & Then
        assertEquals("test-spreadsheet-id", googleSheetsProperties.spreadsheetId)
        assertEquals("Sheet1!B2:G2", googleSheetsProperties.titleRange)
        assertEquals("Sheet1!C3:G", googleSheetsProperties.dataRange)
    }

    @Test
    fun `스프링_부트_컨텍스트에서_TelegramProperties_자동_바인딩_테스트`() {
        // When & Then
        assertEquals("test-bot", telegramProperties.botId)
        assertEquals("test-token", telegramProperties.botToken)
        assertEquals("https://api.telegram.org/bot", telegramProperties.baseUrl)
        assertEquals("-1001234567890", telegramProperties.testChatId)
        assertEquals("-1001234567891", telegramProperties.firstStudyChatId)
    }

    @Test
    fun `Configuration_Properties_빈_등록_확인_테스트`() {
        // When & Then
        assertNotNull(googleSheetsProperties)
        assertNotNull(telegramProperties)
        
        // 프로퍼티 값들이 정상적으로 바인딩되었는지 확인
        assertFalse(googleSheetsProperties.spreadsheetId.isEmpty())
        assertFalse(telegramProperties.botId.isEmpty())
    }

    @Test
    fun `ConfigurationProperties_어노테이션_동작_확인_테스트`() {
        // When & Then: 실제 프로퍼티 값들이 올바른 prefix로부터 바인딩되었는지 확인
        // test properties에서 google.sheets.spreadsheet-id로 설정한 값이 정상 바인딩되는지
        assertEquals("test-spreadsheet-id", googleSheetsProperties.spreadsheetId)
        
        // test properties에서 telegram.bot-id로 설정한 값이 정상 바인딩되는지  
        assertEquals("test-bot", telegramProperties.botId)
    }
}