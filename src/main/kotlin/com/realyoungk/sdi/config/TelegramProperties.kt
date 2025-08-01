package com.realyoungk.sdi.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * application.yml의 'telegram' 경로에 있는 설정값들을 바인딩하는 클래스입니다.
 *
 * @param botId 텔레그램 봇의 ID
 * @param botToken 텔레그램 봇의 인증 토큰
 * @param baseUrl 텔레그램 API 기본 URL
 * @param testChatId 테스트용 채팅방 ID
 * @param firstStudyChatId 1기 스터디 채팅방 ID
 */
@ConfigurationProperties(prefix = "telegram")
data class TelegramProperties(
    val botId: String,
    val botToken: String,
    val baseUrl: String,
    val testChatId: String,
    val firstStudyChatId: String
)