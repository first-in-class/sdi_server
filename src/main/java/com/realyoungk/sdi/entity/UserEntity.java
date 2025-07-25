package com.realyoungk.sdi.entity;

import com.realyoungk.sdi.model.CalendarType; // import 추가
import com.realyoungk.sdi.model.LunarMonthType;
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
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String publicId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 20)
    private String phoneNumber;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private CalendarType calendarType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LunarMonthType lunarMonthType = LunarMonthType.NORMAL;


    @Column(length = 100)
    private String teamName;

    @PrePersist
    protected void onCreate() {
        if (this.publicId == null) {
            this.publicId = UUID.randomUUID().toString();
        }
    }

    @Builder
    public UserEntity(String name, String phoneNumber, LocalDate birthday, CalendarType calendarType, LunarMonthType lunarMonthType, String teamName) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.calendarType = calendarType;
        this.lunarMonthType = (calendarType == CalendarType.SOLAR) ? LunarMonthType.NORMAL : lunarMonthType;
        this.teamName = teamName;
    }
}
