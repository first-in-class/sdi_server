package com.realyoungk.sdi.repository;

import com.realyoungk.sdi.config.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TelegramRepository {
    final private TelegramProperties telegramProperties;
    final private RestTemplate restTemplate;

    public void sendMessage(String chatId, String message) {
        String url = telegramProperties.baseUrl() + telegramProperties.botToken() + "/sendMessage";

        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("chat_id", chatId)
                .queryParam("text", message)
                .build(true)
                .toUri();

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Message sent successfully to chat_id: {}", chatId);
        } else {
            log.error("Failed to send message: {} - {}", response.getStatusCode(), response.getBody());
        }
    }
}