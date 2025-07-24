package com.realyoungk.sdi.model;

import com.realyoungk.sdi.entity.UserEntity;

import java.time.LocalDate;

public record UserModel(
        String id, // 외부에 노출할 아이디
        String name, // 이름
        String phoneNumber, // 휴대폰번호
        LocalDate birthday, // 생일
        CalendarType calendarType, // 양력/음력 구분
        String teamName // 소속 스터디명
) {
    public static UserModel from(UserEntity entity) {
        return new UserModel(
                entity.getId(),
                entity.getName(),
                entity.getPhoneNumber(),
                entity.getBirthday(),
                entity.getCalendarType(),
                entity.getTeamName()
        );
    }
}