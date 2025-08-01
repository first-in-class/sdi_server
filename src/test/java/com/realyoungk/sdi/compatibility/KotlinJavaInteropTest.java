package com.realyoungk.sdi.compatibility;

import com.realyoungk.sdi.config.GoogleSheetsProperties;
import com.realyoungk.sdi.config.TelegramProperties;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KotlinJavaInteropTest {

    @Test
    void 자바에서_코틀린_데이터_클래스_getter_메서드_호출_테스트() {
        // Given: Kotlin data class를 Java에서 사용
        GoogleSheetsProperties googleProperties = new GoogleSheetsProperties(
            "test-spreadsheet-id",
            "Sheet1!B2:G2", 
            "Sheet1!C3:G"
        );
        
        TelegramProperties telegramProperties = new TelegramProperties(
            "test-bot",
            "test-token",
            "https://api.telegram.org/bot",
            "-1001234567890",
            "-1001234567891"
        );

        // When & Then: Java에서 Kotlin data class의 getter 메서드 호출이 정상 동작하는지 확인
        assertEquals("test-spreadsheet-id", googleProperties.getSpreadsheetId());
        assertEquals("Sheet1!B2:G2", googleProperties.getTitleRange());
        assertEquals("Sheet1!C3:G", googleProperties.getDataRange());
        
        assertEquals("test-bot", telegramProperties.getBotId());
        assertEquals("test-token", telegramProperties.getBotToken());
        assertEquals("https://api.telegram.org/bot", telegramProperties.getBaseUrl());
        assertEquals("-1001234567890", telegramProperties.getTestChatId());
        assertEquals("-1001234567891", telegramProperties.getFirstStudyChatId());
    }

    @Test
    void 자바에서_코틀린_데이터_클래스_기본_메서드_테스트() {
        // Given
        GoogleSheetsProperties properties1 = new GoogleSheetsProperties("id1", "range1", "range2");
        GoogleSheetsProperties properties2 = new GoogleSheetsProperties("id1", "range1", "range2");
        GoogleSheetsProperties properties3 = new GoogleSheetsProperties("id2", "range1", "range2");
        
        // When & Then: Java에서 Kotlin data class의 기본 메서드들이 정상 동작하는지 확인
        
        // 1. equals() 메서드
        assertEquals(properties1, properties2);
        assertNotEquals(properties1, properties3);
        
        // 2. hashCode() 메서드
        assertEquals(properties1.hashCode(), properties2.hashCode());
        assertNotEquals(properties1.hashCode(), properties3.hashCode());
        
        // 3. toString() 메서드
        String toStringResult = properties1.toString();
        assertTrue(toStringResult.contains("GoogleSheetsProperties"));
        assertTrue(toStringResult.contains("id1"));
    }

    @Test
    void Spring_Configuration_Properties_Java_호환성_테스트() {
        // Given: Spring Boot에서 사용되는 패턴이 Java에서 정상 동작하는지 확인
        TelegramProperties properties = new TelegramProperties(
            "spring-bot",
            "spring-token", 
            "https://spring.test.com",
            "spring-test-chat",
            "spring-study-chat"
        );

        // When & Then: Spring Boot Configuration Properties가 Java에서 정상 동작하는지 확인
        
        // 1. 모든 필드 접근 가능
        assertNotNull(properties.getBotId());
        assertNotNull(properties.getBotToken());
        assertNotNull(properties.getBaseUrl());
        assertNotNull(properties.getTestChatId());
        assertNotNull(properties.getFirstStudyChatId());
        
        // 2. 값 검증
        assertEquals("spring-bot", properties.getBotId());
        assertEquals("spring-token", properties.getBotToken());
        assertEquals("https://spring.test.com", properties.getBaseUrl());
        assertEquals("spring-test-chat", properties.getTestChatId());
        assertEquals("spring-study-chat", properties.getFirstStudyChatId());
    }
}