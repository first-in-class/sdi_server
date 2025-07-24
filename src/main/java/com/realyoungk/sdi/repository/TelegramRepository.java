package com.realyoungk.sdi.repository;

import com.realyoungk.sdi.config.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TelegramRepository {
    final private TelegramProperties telegramProperties;

    public void sendMessage(String chatId, String message) {
        RestTemplate restTemplate = new RestTemplate();
        String url = telegramProperties.baseUrl() + telegramProperties.botToken() + "/sendMessage?chat_id=" + chatId + "&text=" + message;

        System.out.print(url);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Message sent successfully: {}", message);
        } else {
            log.error("Failed to send message: {} - {}", response.getStatusCode(), response.getBody());
        }
    }
}
