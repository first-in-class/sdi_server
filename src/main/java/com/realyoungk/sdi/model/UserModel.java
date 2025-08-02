package com.realyoungk.sdi.model;

import com.github.fj.koreanlunarcalendar.KoreanLunarCalendarUtils;
import com.github.fj.koreanlunarcalendar.KoreanLunarDate;
import com.realyoungk.sdi.entity.UserEntity;
import com.realyoungk.sdi.enums.CalendarType;
import com.realyoungk.sdi.enums.LunarMonthType;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public record UserModel(
        String id,
        String name,
        String phoneNumber,
        String teamName,
        LocalDate birthday,
        CalendarType calendarType,
        LunarMonthType lunarMonthType
) {
    public static UserModel from(UserEntity entity) {
        return new UserModel(
                entity.getPublicId(),
                entity.getName(),
                entity.getPhoneNumber(),
                entity.getTeamName(),
                entity.getBirthday(),
                entity.getCalendarType(),
                entity.getLunarMonthType()
        );
    }

    public LocalDate getSolarBirthdayThisYear(int year) {
        if (this.birthday == null || this.calendarType == null) {
            return null;
        }

        return switch (this.calendarType) {
            case SOLAR -> this.birthday.withYear(year);
            case LUNAR -> convertLunarToSolarWithApproximation(this.birthday, year);
        };
    }

    private LocalDate convertLunarToSolarWithApproximation(LocalDate lunarDate, int targetYear) {
        int month = lunarDate.getMonthValue();
        int day = lunarDate.getDayOfMonth();
        boolean isLeap = (this.lunarMonthType == LunarMonthType.LEAP);

        try {
            KoreanLunarDate result = KoreanLunarCalendarUtils.getSolarDateOf(
                    targetYear, month, day, isLeap
            );
            return LocalDate.of(result.solYear, result.solMonth, result.solDay);
        } catch (Exception e) {
            if (day == 30) {
                log.warn("음력 30일 변환 실패, 근사치(29일)로 재시도합니다. lunarDate={}, isLeap={}, targetYear={}", lunarDate, isLeap, targetYear);
                try {
                    KoreanLunarDate result = KoreanLunarCalendarUtils.getSolarDateOf(
                            targetYear, month, 29, isLeap
                    );
                    return LocalDate.of(result.solYear, result.solMonth, result.solDay);
                } catch (Exception e2) {
                    log.error("음력 변환 최종 실패 (근사치 29일 시도 포함): lunarDate={}, isLeap={}, targetYear={}", lunarDate, isLeap, targetYear, e2);
                    return null;
                }
            } else {
                log.warn("음력 변환 실패: lunarDate={}, isLeap={}, targetYear={}", lunarDate, isLeap, targetYear, e);
                return null;
            }
        }
    }
}