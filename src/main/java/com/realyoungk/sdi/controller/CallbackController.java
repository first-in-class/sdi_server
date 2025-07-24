package com.realyoungk.sdi.controller;

import com.realyoungk.sdi.dto.TelegramUpdateDto;
import com.realyoungk.sdi.service.NotificationService;
import com.realyoungk.sdi.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/callbacks", method = {RequestMethod.POST})
public class CallbackController {
    private final VisitService visitService;
    private final NotificationService notificationService;

    @PostMapping("/telegram")
    public void postTelegram(@RequestBody TelegramUpdateDto update) {
        if (update == null || update.message() == null || update.message().text() == null) {
            log.warn("Received an empty or invalid update from Telegram.");
            return;
        }

        String text = update.message().text();
        String chatId = String.valueOf(update.message().chat().id());
        log.info("Received message '{}' from chat_id '{}'", text, chatId);

        if ("/visits".equalsIgnoreCase(text.trim())) {
            final String visitsMessage = visitService.createMessage();
            notificationService.send(chatId, visitsMessage);
            log.info("Delegated /visits command for chat_id '{}' to VisitService", chatId);
        }
    }
}
