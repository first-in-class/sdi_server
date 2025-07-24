package com.realyoungk.sdi.entity;

import com.realyoungk.sdi.model.CalendarType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internalId; // DB 내부에서만 사용할 숫자 ID (성능 최적화)

    @Column(nullable = false, unique = true, updatable = false)
    private String id; // 외부에 노출될 고유 ID (UUID)

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 20)
    private String phoneNumber;

    private LocalDate birthday; // 생년월일 (YYYY-MM-DD)

    @Enumerated(EnumType.STRING)
    private CalendarType calendarType; // 양력/음력 구분

    @Column(length = 100)
    private String teamName; // 소속 스터디명

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Builder
    public UserEntity(String name, String phoneNumber, LocalDate birthday, CalendarType calendarType, String teamName) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.calendarType = calendarType;
        this.teamName = teamName;
    }
}