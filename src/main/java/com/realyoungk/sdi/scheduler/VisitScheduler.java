package com.realyoungk.sdi.scheduler;

import com.realyoungk.sdi.config.TelegramProperties;
import com.realyoungk.sdi.controller.CallbackController;
import com.realyoungk.sdi.formatter.TelegramMessageFormatter;
import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.service.NotificationService;
import com.realyoungk.sdi.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VisitScheduler {

    private final VisitService visitService;
    private final NotificationService notificationService;
    private final TelegramProperties telegramProperties;
    private final TelegramMessageFormatter telegramMessageFormatter;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendDailyVisitNotification() {
        log.info("매일 아침 8시 탐방 일정 알림 배치를 시작합니다.");
        final List<VisitModel> visits = visitService.fetchUpcoming();
        final String visitsMessage = telegramMessageFormatter.formatVisitsMessage(visits);
        final String chatId = telegramProperties.getFirstStudyChatId();
        notificationService.send(chatId, visitsMessage);
        log.info("Chat ID '{}'로 탐방 일정 알림을 성공적으로 전송했습니다.", chatId);
    }
}
