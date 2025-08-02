package com.realyoungk.sdi.service;

import com.realyoungk.sdi.repository.TelegramRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service("telegramNotificationService")
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {

    private final TelegramRepository telegramRepository;

    @Override
    public void send(@NotNull String chatId, @NotNull String message) {
        telegramRepository.sendMessage(chatId, message);
    }
}