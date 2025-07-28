package com.realyoungk.sdi.scheduler;

import com.realyoungk.sdi.config.TelegramProperties;
import com.realyoungk.sdi.service.NotificationService;
import com.realyoungk.sdi.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VisitScheduler {

    private final VisitService visitService;
    private final NotificationService notificationService;
    private final TelegramProperties telegramProperties;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendDailyVisitNotification() {
        log.info("매일 아침 8시 탐방 일정 알림 배치를 시작합니다.");
        final String visitsMessage = visitService.createMessage();
        final String chatId = telegramProperties.firstStudyChatId();
        notificationService.send(chatId, visitsMessage);
        log.info("Chat ID '{}'로 탐방 일정 알림을 성공적으로 전송했습니다.", chatId);
    }
}