package com.realyoungk.sdi.service

import com.realyoungk.sdi.repository.TelegramRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class NotificationServiceTest {

    @Mock
    private lateinit var telegramRepository: TelegramRepository

    @InjectMocks
    private lateinit var telegramNotificationService: TelegramNotificationService

    @Test
    fun `NotificationService send 메서드 인터페이스 호출 테스트`() {
        // Given
        val chatId = "123456789"
        val message = "테스트 메시지"

        // When
        telegramNotificationService.send(chatId, message)

        // Then
        verify(telegramRepository).sendMessage(chatId, message)
    }

    @Test
    fun `TelegramNotificationService가 NotificationService 인터페이스를 구현하는지 테스트`() {
        // Given & When & Then
        assert(telegramNotificationService is NotificationService)
    }

    @Test
    fun `NotificationService 인터페이스 메서드 시그니처 테스트`() {
        // Given
        val notificationService: NotificationService = telegramNotificationService
        
        // When & Then - 컴파일 타임에 메서드 존재 여부 확인
        notificationService.send("test", "message")
    }
}