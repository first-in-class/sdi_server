package com.realyoungk.sdi.repository;

import com.realyoungk.sdi.entity.UserEntity;
import com.realyoungk.sdi.enums.CalendarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByPublicId(String publicId);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    List<UserEntity> findByName(String name);

    @Query("SELECT u FROM UserEntity u WHERE u.calendarType = com.realyoungk.sdi.enums.CalendarType.SOLAR AND EXTRACT(MONTH FROM u.birthday) = :month AND EXTRACT(DAY FROM u.birthday) = :day")
    List<UserEntity> findUsersWithSolarBirthday(@Param("month") int month, @Param("day") int day);

    @Query("SELECT u FROM UserEntity u WHERE u.calendarType = com.realyoungk.sdi.enums.CalendarType.SOLAR AND EXTRACT(MONTH FROM u.birthday) = :month")
    List<UserEntity> findUsersWithSolarBirthdayInMonth(@Param("month") int month);

    List<UserEntity> findByCalendarType(CalendarType calendarType);
}