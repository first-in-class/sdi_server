package com.realyoungk.sdi.controller;

import com.realyoungk.sdi.dto.TelegramUpdateDto;
import com.realyoungk.sdi.formatter.TelegramMessageFormatter;
import com.realyoungk.sdi.model.UserModel;
import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.service.NotificationService;
import com.realyoungk.sdi.service.UserService;
import com.realyoungk.sdi.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/callbacks")
public class CallbackController {
    private final VisitService visitService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final TelegramMessageFormatter telegramMessageFormatter;

    @PostMapping("/telegram")
    public void postTelegram(@RequestBody TelegramUpdateDto update) {
        if (update == null || update.message() == null || update.message().text() == null) {
            log.warn("Received an empty or invalid update from Telegram.");
            return;
        }

        String text = update.message().text().trim();
        String chatId = String.valueOf(update.message().chat().id());
        log.info("Received message '{}' from chat_id '{}'", text, chatId);


        if ("/visits".equalsIgnoreCase(text)) {
            List<VisitModel> visitModels = visitService.fetchUpcoming();
            final String visitsMessage = telegramMessageFormatter.formatVisitsMessage(visitModels);
            notificationService.send(chatId, visitsMessage);
            log.info("Delegated /visits command for chat_id '{}' to VisitService", chatId);
        }

        if ("/users?birthday=today".equalsIgnoreCase(text)) {
            List<UserModel> users = userService.findUsersWithBirthdayOn(LocalDate.now());
            String birthdayMessage = telegramMessageFormatter.formatBirthdayMessage(users, "오늘");
            notificationService.send(chatId, birthdayMessage);
            log.info("Processed /users?birthday=today command for chat_id '{}'", chatId);
        }

        if ("/users?birthday=this-month".equalsIgnoreCase(text)) {
            List<UserModel> users = userService.findUsersWithBirthdayIn(LocalDate.now());
            String birthdayMessage = telegramMessageFormatter.formatBirthdayMessage(users, "이번 달");
            notificationService.send(chatId, birthdayMessage);
            log.info("Processed /users?birthday=this-month command for chat_id '{}'", chatId);
        }
    }
}
