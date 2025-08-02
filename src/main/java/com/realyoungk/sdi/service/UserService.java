package com.realyoungk.sdi.service;

import com.github.fj.koreanlunarcalendar.KoreanLunarCalendarUtils;
import com.github.fj.koreanlunarcalendar.KoreanLunarDate;
import com.realyoungk.sdi.entity.UserEntity;
import com.realyoungk.sdi.enums.CalendarType;
import com.realyoungk.sdi.enums.LunarMonthType;
import com.realyoungk.sdi.model.UserModel;
import com.realyoungk.sdi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 모든 유저를 조회합니다.
     */
    public List<UserModel> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserModel::from)
                .toList();
    }

    /**
     * 사용자를 생성합니다.
     */
    public UserModel createUser(UserModel body) {
        UserEntity entity = UserEntity.builder()
                .name(body.name())
                .phoneNumber(body.phoneNumber())
                .birthday(body.birthday())
                .calendarType(body.calendarType())
                .lunarMonthType(body.lunarMonthType())
                .teamName(body.teamName())
                .build();

        UserEntity savedEntity = userRepository.save(entity);
        return UserModel.from(savedEntity);
    }

    /**
     * 특정 publicId를 가진 사용자를 조회합니다.
     */
    public Optional<UserModel> findUserByPublicId(String publicId) {
        return userRepository.findByPublicId(publicId)
                .map(UserModel::from);
    }

    /**
     * 특정 날짜가 생일인 모든 사용자(양력/음력 포함)를 조회합니다.
     */
    public List<UserModel> findUsersWithBirthdayOn(LocalDate date) {
        List<UserEntity> solarBirthdayUsers = userRepository.findUsersWithSolarBirthday(
                date.getMonthValue(),
                date.getDayOfMonth()
        );

        List<UserEntity> lunarBirthdayUsers = findLunarBirthdayUsersOn(date);

        return Stream.concat(solarBirthdayUsers.stream(), lunarBirthdayUsers.stream())
                .distinct() // 중복 제거
                .map(UserModel::from)
                .toList();
    }

    /**
     * 특정 날짜가 속한 달이 생일인 모든 사용자(양력/음력 포함)를 조회합니다.
     */
    public List<UserModel> findUsersWithBirthdayIn(LocalDate date) {
        int targetMonth = date.getMonthValue();

        List<UserEntity> solarBirthdayUsers = userRepository.findUsersWithSolarBirthdayInMonth(targetMonth);

        List<UserEntity> lunarBirthdayUsers = findLunarBirthdayUsersIn(targetMonth);

        return Stream.concat(solarBirthdayUsers.stream(), lunarBirthdayUsers.stream())
                .distinct()
                .map(UserModel::from)
                .toList();
    }

    /**
     * ✅ [추가] 여러 사용자를 한번에 생성합니다.
     */
    public List<UserModel> createUsers(List<UserModel> users) {
        List<UserEntity> entitiesToSave = users.stream()
                .map(body -> UserEntity.builder()
                        .name(body.name())
                        .phoneNumber(body.phoneNumber())
                        .birthday(body.birthday())
                        .calendarType(body.calendarType())
                        .lunarMonthType(body.lunarMonthType())
                        .teamName(body.teamName())
                        .build())
                .toList();

        List<UserEntity> savedEntities = userRepository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(UserModel::from)
                .toList();
    }


    private List<UserEntity> findLunarBirthdayUsersOn(LocalDate targetSolarDate) {
        List<UserEntity> allLunarUsers = userRepository.findByCalendarType(CalendarType.LUNAR);

        return allLunarUsers.stream()
                .filter(user -> {
                    LocalDate solarBirthdayThisYear = convertLunarToSolar(
                            user.getBirthday(),
                            targetSolarDate.getYear(),
                            user.getLunarMonthType() == LunarMonthType.LEAP
                    );
                    return targetSolarDate.equals(solarBirthdayThisYear);
                })
                .toList();
    }

    private List<UserEntity> findLunarBirthdayUsersIn(int targetMonth) {
        List<UserEntity> allLunarUsers = userRepository.findByCalendarType(CalendarType.LUNAR);
        int currentYear = LocalDate.now().getYear();

        return allLunarUsers.stream()
                .filter(user -> {
                    LocalDate solarBirthdayThisYear = convertLunarToSolar(
                            user.getBirthday(),
                            currentYear,
                            user.getLunarMonthType() == LunarMonthType.LEAP
                    );
                    return solarBirthdayThisYear != null && solarBirthdayThisYear.getMonthValue() == targetMonth;
                })
                .toList();
    }

    /**
     * 사용자의 음력 생일을 특정 년도의 양력 생일로 변환합니다.
     * 알려주신 static 메서드 호출 방식으로 수정했습니다.
     */
    private LocalDate convertLunarToSolar(LocalDate lunarDate, int targetYear, boolean isLeapMonth) {
        try {
            KoreanLunarDate result = KoreanLunarCalendarUtils.getSolarDateOf(
                    targetYear,
                    lunarDate.getMonthValue(),
                    lunarDate.getDayOfMonth(),
                    isLeapMonth
            );
            return LocalDate.of(result.solYear, result.solMonth, result.solDay);
        } catch (Exception e) {
            log.warn("음력 변환 실패: lunarDate={}, targetYear={}", lunarDate, targetYear, e);
            return null;
        }
    }
}