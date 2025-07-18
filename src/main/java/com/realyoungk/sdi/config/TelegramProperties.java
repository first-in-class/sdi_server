package com.realyoungk.sdi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram.bot")
public record TelegramProperties(String username, String token) {
}