package com.realyoungk.sdi.dto

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TelegramDtoTest {

    private val objectMapper = ObjectMapper()

    @Test
    fun `TelegramChatDto_JSON_직렬화_역직렬화_테스트`() {
        // Given
        val chatDto = TelegramChatDto(id = 123456L)
        
        // When: 직렬화
        val json = objectMapper.writeValueAsString(chatDto)
        
        // Then: JSON 구조 확인
        assertTrue(json.contains("\"id\":123456"))
        
        // When: 역직렬화
        val deserializedDto = objectMapper.readValue(json, TelegramChatDto::class.java)
        
        // Then: 원본과 동일한지 확인
        assertEquals(chatDto, deserializedDto)
        assertEquals(123456L, deserializedDto.id)
    }

    @Test
    fun `TelegramChatDto_데이터_클래스_기본_기능_테스트`() {
        // Given
        val chatDto1 = TelegramChatDto(id = 123L)
        val chatDto2 = TelegramChatDto(id = 123L)
        val chatDto3 = TelegramChatDto(id = 456L)
        
        // When & Then: equals 테스트
        assertEquals(chatDto1, chatDto2)
        assertNotEquals(chatDto1, chatDto3)
        
        // When & Then: hashCode 테스트
        assertEquals(chatDto1.hashCode(), chatDto2.hashCode())
        assertNotEquals(chatDto1.hashCode(), chatDto3.hashCode())
        
        // When & Then: toString 테스트
        val toStringResult = chatDto1.toString()
        assertTrue(toStringResult.contains("TelegramChatDto"))
        assertTrue(toStringResult.contains("123"))
    }

    @Test
    fun `TelegramMessageDto_JSON_직렬화_역직렬화_테스트`() {
        // Given
        val chatDto = TelegramChatDto(id = 123456L)
        val messageDto = TelegramMessageDto(
            messageId = 789L,
            chat = chatDto,
            text = "Hello, World!"
        )
        
        // When: 직렬화
        val json = objectMapper.writeValueAsString(messageDto)
        
        // Then: JSON 구조 확인 (실제 생성된 JSON 구조 확인)
        println("Generated JSON: $json")
        assertTrue(json.contains("789"))
        assertTrue(json.contains("\"Hello, World!\""))
        assertTrue(json.contains("123456"))
        
        // When: 역직렬화
        val deserializedDto = objectMapper.readValue(json, TelegramMessageDto::class.java)
        
        // Then: 원본과 동일한지 확인
        assertEquals(messageDto, deserializedDto)
        assertEquals(789L, deserializedDto.messageId)
        assertEquals("Hello, World!", deserializedDto.text)
        assertEquals(123456L, deserializedDto.chat.id)
    }

    @Test
    fun `TelegramUpdateDto_JSON_직렬화_역직렬화_테스트`() {
        // Given
        val chatDto = TelegramChatDto(id = 123456L)
        val messageDto = TelegramMessageDto(
            messageId = 789L,
            chat = chatDto,
            text = "Test message"
        )
        val updateDto = TelegramUpdateDto(
            updateId = 999L,
            message = messageDto
        )
        
        // When: 직렬화
        val json = objectMapper.writeValueAsString(updateDto)
        
        // Then: JSON 구조 확인 (실제 생성된 JSON 구조 확인)
        println("Generated UpdateDto JSON: $json")
        assertTrue(json.contains("999"))
        assertTrue(json.contains("message"))
        
        // When: 역직렬화
        val deserializedDto = objectMapper.readValue(json, TelegramUpdateDto::class.java)
        
        // Then: 원본과 동일한지 확인
        assertEquals(updateDto, deserializedDto)
        assertEquals(999L, deserializedDto.updateId)
        assertEquals(messageDto, deserializedDto.message)
    }

    @Test
    fun `Jackson_어노테이션_동작_확인_테스트`() {
        // Given: 실제 Telegram API 형태의 JSON
        val telegramApiJson = """
        {
            "update_id": 12345,
            "message": {
                "message_id": 67890,
                "chat": {
                    "id": -1001234567890,
                    "type": "supergroup",
                    "title": "Test Group"
                },
                "text": "Hello from Telegram!",
                "date": 1234567890
            }
        }
        """.trimIndent()
        
        // When: JSON을 DTO로 역직렬화
        val updateDto = objectMapper.readValue(telegramApiJson, TelegramUpdateDto::class.java)
        
        // Then: 필드 매핑 확인
        assertEquals(12345L, updateDto.updateId)
        assertEquals(67890L, updateDto.message.messageId)
        assertEquals(-1001234567890L, updateDto.message.chat.id)
        assertEquals("Hello from Telegram!", updateDto.message.text)
    }

    @Test
    fun `JsonIgnoreProperties_동작_확인_테스트`() {
        // Given: 알 수 없는 필드가 포함된 JSON
        val jsonWithUnknownFields = """
        {
            "id": 123456,
            "unknown_field": "should be ignored",
            "another_unknown": 999
        }
        """.trimIndent()
        
        // When: JSON을 DTO로 역직렬화 (예외가 발생하지 않아야 함)
        val chatDto = objectMapper.readValue(jsonWithUnknownFields, TelegramChatDto::class.java)
        
        // Then: 알려진 필드만 정상적으로 매핑되어야 함
        assertEquals(123456L, chatDto.id)
    }

    @Test
    fun `데이터_클래스_copy_기능_테스트`() {
        // Given
        val originalChat = TelegramChatDto(id = 123L)
        val originalMessage = TelegramMessageDto(
            messageId = 456L,
            chat = originalChat,
            text = "Original text"
        )
        
        // When: copy 함수 사용
        val copiedMessage = originalMessage.copy(text = "Modified text")
        
        // Then: 일부 필드만 변경되고 나머지는 동일해야 함
        assertEquals("Modified text", copiedMessage.text)
        assertEquals(456L, copiedMessage.messageId) // 변경되지 않음
        assertEquals(originalChat, copiedMessage.chat) // 변경되지 않음
        assertNotEquals(originalMessage, copiedMessage)
    }
}