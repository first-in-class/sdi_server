package com.realyoungk.sdi.scheduler;

import com.realyoungk.sdi.config.TelegramProperties;
import com.realyoungk.sdi.formatter.TelegramMessageFormatter;
import com.realyoungk.sdi.model.UserModel;
import com.realyoungk.sdi.service.NotificationService;
import com.realyoungk.sdi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BirthdayScheduler {

    private final UserService userService;
    private final NotificationService notificationService;
    private final TelegramMessageFormatter messageFormatter;
    private final TelegramProperties telegramProperties;

    /**
     * 매일 아침 8시에 오늘 생일인 사용자를 찾아 알림을 보냅니다.
     * 생일자가 없으면 아무 작업도 하지 않습니다.
     * cron = "초 분 시 일 월 요일"
     */
    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
    public void sendDailyBirthdayNotification() {
        log.info("매일 생일자 확인 배치를 시작합니다.");
        List<UserModel> usersWithBirthdayToday = userService.findUsersWithBirthdayOn(LocalDate.now());

        // ✅ 오늘 생일인 사람이 있을 경우에만 메시지를 보냅니다.
        if (usersWithBirthdayToday != null && !usersWithBirthdayToday.isEmpty()) {
            String birthdayMessage = messageFormatter.formatBirthdayMessage(usersWithBirthdayToday, "오늘");
            String chatId = telegramProperties.firstStudyChatId(); // 알림을 보낼 채팅방 ID

            notificationService.send(chatId, birthdayMessage);
            log.info("오늘의 생일자 알림을 성공적으로 전송했습니다. 대상: {} 명", usersWithBirthdayToday.size());
        } else {
            log.info("오늘 생일인 사용자가 없어 알림을 보내지 않습니다.");
        }
    }

    /**
     * 매월 1일 오전 9시에 이번 달 생일인 사용자 목록을 알립니다.
     */
    @Scheduled(cron = "0 0 8 1 * *", zone = "Asia/Seoul")
    public void sendMonthlyBirthdayNotification() {
        log.info("매월 생일자 목록 확인 배치를 시작합니다.");
        List<UserModel> usersWithBirthdayThisMonth = userService.findUsersWithBirthdayIn(LocalDate.now());
        String birthdayMessage = messageFormatter.formatBirthdayMessage(usersWithBirthdayThisMonth, "이번 달");
        String chatId = telegramProperties.firstStudyChatId(); // 알림을 보낼 채팅방 ID

        notificationService.send(chatId, birthdayMessage);
        log.info("이번 달의 생일자 목록 알림을 성공적으로 전송했습니다.");
    }
}