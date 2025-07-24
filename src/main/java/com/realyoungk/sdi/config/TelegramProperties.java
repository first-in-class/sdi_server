package com.realyoungk.sdi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram")
public record TelegramProperties(String botId, String botToken, String baseUrl, String testChatId) {
}