package com.realyoungk.sdi.formatter;

import com.realyoungk.sdi.model.UserModel;
import com.realyoungk.sdi.model.VisitModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class TelegramMessageFormatter {

    /**
     * 방문 일정 목록을 받아 텔레그램 메시지 형식으로 변환합니다.
     * (VisitService에 있던 로직을 이동)
     *
     * @param visitModels 방문 일정 목록
     * @return 텔레그램 메시지 문자열
     */
    public String formatVisitsMessage(List<VisitModel> visitModels) {
        if (visitModels == null || visitModels.isEmpty()) {
            return "다가오는 탐방 일정이 없습니다. 😅";
        }

        final Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        StringBuilder sb = new StringBuilder();
        sb.append("📢 [다가오는 탐방 일정]\n\n");

        for (VisitModel visitModel : visitModels) {
            long diff = (visitModel.getStartedAt().getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
            boolean isSoon = diff >= 0 && diff <= 2;
            if (isSoon) {
                sb.append(visitModel.toDetailedString(diff));
                sb.append("\n");
            } else {
                sb.append(visitModel.toSimpleString(diff));
            }
        }
        return sb.toString();
    }

    /**
     * 생일자 목록을 받아 텔레그램 메시지 형식으로 변환합니다.
     * (CallbackController에 있던 로직을 이동)
     *
     * @param users  생일자 목록
     * @param period "오늘", "이번 달" 등 기간을 나타내는 문자열
     * @return 텔레그램 메시지 문자열
     */
    public String formatBirthdayMessage(List<UserModel> users, String period) {
        if (users == null || users.isEmpty()) {
            return String.format("🎂 %s 생일인 분이 없습니다.", period);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("🎉 %s 생일인 분들입니다! 🎉\n\n", period));

        int currentYear = LocalDate.now().getYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN);

        users.forEach(user -> {
            LocalDate birthdayThisYear = user.getSolarBirthdayThisYear(currentYear);
            if (birthdayThisYear != null) {
                sb.append(String.format("- %s님 (%s)\n", user.name(), birthdayThisYear.format(formatter)));
            } else {
                sb.append(String.format("- %s님\n", user.name()));
            }
        });

        sb.append("\n모두 축하해주세요! 🥳");
        return sb.toString();
    }
}