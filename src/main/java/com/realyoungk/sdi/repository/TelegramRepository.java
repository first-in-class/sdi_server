package com.realyoungk.sdi.repository;

import com.realyoungk.sdi.config.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TelegramRepository {
    final private TelegramProperties telegramProperties;
    final private RestTemplate restTemplate;

    public void sendMessage(String chatId, String message) {
        String url = telegramProperties.getBaseUrl() + telegramProperties.getBotToken() + "/sendMessage";

        Map<String, String> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("text", message);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Message sent successfully to chat_id: {}", chatId);
            } else {
                log.error("Failed to send message: {} - {}", response.getStatusCode(), response.getBody());
            }
        } catch (RestClientException e) {
            log.error("Exception occurred while sending message to Telegram API", e);
        }
    }
}