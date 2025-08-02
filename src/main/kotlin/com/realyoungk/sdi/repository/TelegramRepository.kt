package com.realyoungk.sdi.repository

import com.realyoungk.sdi.config.TelegramProperties
import com.realyoungk.sdi.service.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Repository
class TelegramRepository(
    private val telegramProperties: TelegramProperties,
    private val restTemplate: RestTemplate
) : NotificationService {
    private val log = LoggerFactory.getLogger(TelegramRepository::class.java)

    override fun send(destination: String, message: String) {
        sendMessage(destination, message)
    }

    private fun sendMessage(chatId: String, message: String) {
        val url = "${telegramProperties.baseUrl}${telegramProperties.botToken}/sendMessage"

        val body = mapOf(
            "chat_id" to chatId,
            "text" to message
        )
        
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        
        val requestEntity = HttpEntity(body, headers)

        try {
            val response: ResponseEntity<String> = restTemplate.postForEntity(url, requestEntity, String::class.java)
            if (response.statusCode.is2xxSuccessful) {
                log.info("Message sent successfully to chat_id: {}", chatId)
            } else {
                log.error("Failed to send message: {} - {}", response.statusCode, response.body)
            }
        } catch (e: RestClientException) {
            log.error("Exception occurred while sending message to Telegram API", e)
        }
    }
}