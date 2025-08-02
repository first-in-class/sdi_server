package com.realyoungk.sdi.repository

import com.realyoungk.sdi.config.TelegramProperties
import com.realyoungk.sdi.service.NotificationService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.*
import org.springframework.http.*
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

class TelegramRepositoryTest {

    @Mock
    private lateinit var telegramProperties: TelegramProperties
    
    @Mock
    private lateinit var restTemplate: RestTemplate
    
    private lateinit var telegramRepository: TelegramRepository
    private lateinit var notificationService: NotificationService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        telegramRepository = TelegramRepository(telegramProperties, restTemplate)
        notificationService = telegramRepository

        `when`(telegramProperties.baseUrl).thenReturn("https://api.telegram.org/bot")
        `when`(telegramProperties.botToken).thenReturn("test-token")
    }

    @Test
    fun `send 메서드가 RestTemplate을 호출한다`() {
        // given
        val chatId = "123456789"
        val message = "테스트 메시지"
        
        val responseEntity = ResponseEntity.ok("success")
        `when`(restTemplate.postForEntity(
            anyString(), 
            any(HttpEntity::class.java), 
            eq(String::class.java)
        )).thenReturn(responseEntity)

        // when
        notificationService.send(chatId, message)

        // then
        verify(restTemplate).postForEntity(
            anyString(),
            any(HttpEntity::class.java),
            eq(String::class.java)
        )
    }

    @Test
    fun `send 메서드가 실패 응답을 받아도 예외를 발생시키지 않는다`() {
        // given
        val chatId = "123456789"
        val message = "테스트 메시지"
        
        val responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error")
        `when`(restTemplate.postForEntity(
            anyString(), 
            any(HttpEntity::class.java), 
            eq(String::class.java)
        )).thenReturn(responseEntity)

        // when & then (예외가 발생하지 않아야 함)
        notificationService.send(chatId, message)
        
        verify(restTemplate).postForEntity(
            anyString(),
            any(HttpEntity::class.java),
            eq(String::class.java)
        )
    }

    @Test
    fun `send 메서드가 RestClientException을 처리한다`() {
        // given
        val chatId = "123456789"
        val message = "테스트 메시지"
        
        `when`(restTemplate.postForEntity(
            anyString(), 
            any(HttpEntity::class.java), 
            eq(String::class.java)
        )).thenThrow(RestClientException("Connection error"))

        // when & then (예외가 발생하지 않아야 함)
        notificationService.send(chatId, message)
        
        verify(restTemplate).postForEntity(
            anyString(),
            any(HttpEntity::class.java),
            eq(String::class.java)
        )
    }

    @Test
    fun `TelegramRepository가 NotificationService를 구현한다`() {
        // given & when & then
        assertThat(telegramRepository).isInstanceOf(NotificationService::class.java)
    }
}