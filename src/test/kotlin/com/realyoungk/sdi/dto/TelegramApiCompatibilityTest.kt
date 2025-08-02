package com.realyoungk.sdi.dto

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
class TelegramApiCompatibilityTest {

    private val objectMapper = ObjectMapper()

    @Test
    fun `실제_Telegram_Bot_API_응답_구조_호환성_테스트`() {
        // Given: 실제 Telegram Bot API에서 반환하는 전체 구조
        val realTelegramApiResponse = """
        {
            "update_id": 123456789,
            "message": {
                "message_id": 1234,
                "from": {
                    "id": 987654321,
                    "is_bot": false,
                    "first_name": "John",
                    "last_name": "Doe",
                    "username": "john_doe",
                    "language_code": "en"
                },
                "chat": {
                    "id": -1001234567890,
                    "title": "SDI Test Group",
                    "username": "sdi_test_group",
                    "type": "supergroup",
                    "description": "Test group for SDI bot",
                    "invite_link": "https://t.me/sdi_test_group",
                    "pinned_message": null,
                    "permissions": {
                        "can_send_messages": true,
                        "can_send_media_messages": true
                    }
                },
                "date": 1640995200,
                "text": "/visits",
                "entities": [
                    {
                        "offset": 0,
                        "length": 7,
                        "type": "bot_command"
                    }
                ]
            }
        }
        """.trimIndent()

        // When: 현재 DTO로 파싱 시도
        val result = objectMapper.readValue(realTelegramApiResponse, TelegramUpdateDto::class.java)

        // Then: @JsonIgnoreProperties 덕분에 추가 필드들은 무시되고 필요한 필드들만 파싱됨
        assertEquals(123456789L, result.updateId)
        assertEquals(1234L, result.message.messageId)
        assertEquals(-1001234567890L, result.message.chat.id)
        assertEquals("/visits", result.message.text)
    }

    @Test
    fun `Telegram_그룹_메시지_구조_호환성_테스트`() {
        // Given: 실제 그룹 채팅에서 오는 메시지 구조
        val groupMessageJson = """
        {
            "update_id": 555666777,
            "message": {
                "message_id": 9999,
                "from": {
                    "id": 111222333,
                    "is_bot": false,
                    "first_name": "Alice",
                    "username": "alice_user"
                },
                "chat": {
                    "id": -1001111222333,
                    "title": "1기 스터디룸",
                    "type": "supergroup",
                    "username": "first_study_room"
                },
                "date": 1640995500,
                "text": "/birthday",
                "reply_to_message": {
                    "message_id": 9998,
                    "from": {
                        "id": 444555666,
                        "first_name": "Bob"
                    },
                    "date": 1640995400,
                    "text": "오늘 누구 생일이야?"
                }
            }
        }
        """.trimIndent()

        // When: 파싱 실행
        val result = objectMapper.readValue(groupMessageJson, TelegramUpdateDto::class.java)

        // Then: 핵심 필드들이 정상적으로 파싱됨
        assertEquals(555666777L, result.updateId)
        assertEquals(9999L, result.message.messageId)
        assertEquals(-1001111222333L, result.message.chat.id)
        assertEquals("/birthday", result.message.text)
    }

    @Test
    fun `Telegram_개인_메시지_구조_호환성_테스트`() {
        // Given: 개인 채팅(DM)에서 오는 메시지 구조
        val privateMessageJson = """
        {
            "update_id": 888999000,
            "message": {
                "message_id": 7777,
                "from": {
                    "id": 123456789,
                    "is_bot": false,
                    "first_name": "Charlie",
                    "last_name": "Brown",
                    "username": "charlie_brown",
                    "language_code": "ko"
                },
                "chat": {
                    "id": 123456789,
                    "first_name": "Charlie",
                    "last_name": "Brown",
                    "username": "charlie_brown",
                    "type": "private"
                },
                "date": 1640995600,
                "text": "안녕하세요!"
            }
        }
        """.trimIndent()

        // When: 파싱 실행
        val result = objectMapper.readValue(privateMessageJson, TelegramUpdateDto::class.java)

        // Then: 개인 채팅 구조도 정상 파싱됨
        assertEquals(888999000L, result.updateId)
        assertEquals(7777L, result.message.messageId)
        assertEquals(123456789L, result.message.chat.id)
        assertEquals("안녕하세요!", result.message.text)
    }

    @Test
    fun `필수_필드_누락시_파싱_실패_테스트`() {
        // Given: message 필드가 누락된 JSON
        val incompleteJson = """
        {
            "update_id": 1234
        }
        """.trimIndent()

        // When & Then: 필수 필드 누락 시 예외 발생
        assertThrows<JsonMappingException> {
            objectMapper.readValue(incompleteJson, TelegramUpdateDto::class.java)
        }
    }

    @Test
    fun `text_필드가_null인_메시지_처리_테스트`() {
        // Given: text가 없는 메시지 (이미지, 스티커 등)
        val noTextMessageJson = """
        {
            "update_id": 111222333,
            "message": {
                "message_id": 4444,
                "from": {
                    "id": 555666777,
                    "first_name": "David"
                },
                "chat": {
                    "id": -1001234567890,
                    "type": "supergroup"
                },
                "date": 1640995700,
                "photo": [
                    {
                        "file_id": "AgACAgIAAxkBAAICHGFq...",
                        "file_unique_id": "AQADyBkBAAICHGFq",
                        "file_size": 1234,
                        "width": 90,
                        "height": 60
                    }
                ]
            }
        }
        """.trimIndent()

        // When & Then: text 필드가 필수로 정의되어 있어서 현재는 실패할 것
        // 이는 DTO 설계 개선이 필요함을 보여줌
        assertThrows<JsonMappingException> {
            objectMapper.readValue(noTextMessageJson, TelegramUpdateDto::class.java)
        }
    }

    @Test
    fun `JsonIgnoreProperties_동작_확인_대용량_응답_테스트`() {
        // Given: 많은 추가 필드들이 포함된 복잡한 Telegram API 응답
        val complexTelegramResponse = """
        {
            "update_id": 999888777,
            "message": {
                "message_id": 8888,
                "from": {
                    "id": 777888999,
                    "is_bot": false,
                    "first_name": "Eve",
                    "last_name": "Wilson",
                    "username": "eve_wilson",
                    "language_code": "en",
                    "is_premium": true,
                    "added_to_attachment_menu": false
                },
                "chat": {
                    "id": -1001234567890,
                    "title": "Advanced Test Group",
                    "username": "advanced_test_group",
                    "type": "supergroup",
                    "description": "Advanced testing group with many fields",
                    "invite_link": "https://t.me/advanced_test_group",
                    "pinned_message": null,
                    "permissions": {
                        "can_send_messages": true,
                        "can_send_media_messages": true,
                        "can_send_polls": true,
                        "can_send_other_messages": true,
                        "can_add_web_page_previews": true,
                        "can_change_info": false,
                        "can_invite_users": true,
                        "can_pin_messages": false
                    },
                    "slow_mode_delay": 0,
                    "message_auto_delete_time": null,
                    "has_protected_content": false,
                    "sticker_set_name": null,
                    "can_set_sticker_set": false,
                    "linked_chat_id": null,
                    "location": null
                },
                "date": 1640995800,
                "text": "Complex message test",
                "entities": [
                    {
                        "offset": 0,
                        "length": 7,
                        "type": "bold"
                    }
                ],
                "reply_markup": null,
                "edit_date": null,
                "has_protected_content": false,
                "media_group_id": null,
                "author_signature": null,
                "forward_from": null,
                "forward_from_chat": null,
                "forward_from_message_id": null,
                "forward_signature": null,
                "forward_sender_name": null,
                "forward_date": null,
                "is_automatic_forward": null,
                "reply_to_message": null,
                "via_bot": null,
                "unknown_field_1": "should be ignored",
                "unknown_field_2": 12345,
                "unknown_field_3": true
            },
            "edited_message": null,
            "channel_post": null,
            "edited_channel_post": null,
            "inline_query": null,
            "chosen_inline_result": null,
            "callback_query": null,
            "shipping_query": null,
            "pre_checkout_query": null,
            "poll": null,
            "poll_answer": null,
            "my_chat_member": null,
            "chat_member": null,
            "chat_join_request": null
        }
        """.trimIndent()

        // When: 복잡한 구조를 파싱 시도
        val result = objectMapper.readValue(complexTelegramResponse, TelegramUpdateDto::class.java)

        // Then: @JsonIgnoreProperties 덕분에 모든 추가 필드들이 무시되고 핵심 필드만 파싱됨
        assertEquals(999888777L, result.updateId)
        assertEquals(8888L, result.message.messageId)
        assertEquals(-1001234567890L, result.message.chat.id)
        assertEquals("Complex message test", result.message.text)
        
        // 추가 필드들은 무시되어야 함 (예외가 발생하지 않음)
        assertNotNull(result)
    }
}