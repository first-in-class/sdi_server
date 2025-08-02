package com.realyoungk.sdi.service

import com.realyoungk.sdi.config.TelegramProperties
import com.realyoungk.sdi.repository.TelegramRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.*
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class NotificationServiceTest {

    @Mock
    private lateinit var telegramProperties: TelegramProperties

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var notificationService: NotificationService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val telegramRepository = TelegramRepository(telegramProperties, restTemplate)
        notificationService = telegramRepository

        `when`(telegramProperties.baseUrl).thenReturn("https://api.telegram.org/bot")
        `when`(telegramProperties.botToken).thenReturn("test-token")
    }

    @Test
    fun `NotificationService send 메서드 인터페이스 호출 테스트`() {
        // Given
        val chatId = "123456789"
        val message = "테스트 메시지"
        
        val responseEntity = ResponseEntity.ok("success")
        `when`(restTemplate.postForEntity(
            anyString(), 
            any(HttpEntity::class.java), 
            eq(String::class.java)
        )).thenReturn(responseEntity)

        // When
        notificationService.send(chatId, message)

        // Then
        verify(restTemplate).postForEntity(
            anyString(),
            any(HttpEntity::class.java),
            eq(String::class.java)
        )
    }

    @Test
    fun `TelegramRepository가 NotificationService 인터페이스를 구현하는지 테스트`() {
        // Given & When & Then
        assert(notificationService is NotificationService)
    }

    @Test
    fun `NotificationService 인터페이스 메서드 시그니처 테스트`() {
        // Given
        val responseEntity = ResponseEntity.ok("success")
        `when`(restTemplate.postForEntity(
            anyString(), 
            any(HttpEntity::class.java), 
            eq(String::class.java)
        )).thenReturn(responseEntity)
        
        // When & Then - 컴파일 타임에 메서드 존재 여부 확인
        notificationService.send("test", "message")
    }
}