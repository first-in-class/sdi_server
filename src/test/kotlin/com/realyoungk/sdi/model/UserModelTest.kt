package com.realyoungk.sdi.model

import com.realyoungk.sdi.entity.UserEntity
import com.realyoungk.sdi.enums.CalendarType
import com.realyoungk.sdi.enums.LunarMonthType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDate

class UserModelTest {

    @Test
    fun `UserModel from 메서드가 UserEntity를 올바르게 변환한다`() {
        // given
        val entity = UserEntity.builder()
            .name("홍길동")
            .phoneNumber("010-1234-5678")
            .birthday(LocalDate.of(1990, 5, 15))
            .calendarType(CalendarType.SOLAR)
            .lunarMonthType(LunarMonthType.NORMAL)
            .teamName("개발팀")
            .build()
        
        // UserEntity는 생성 시 publicId가 자동으로 설정됨 (@PrePersist)
        // when
        val userModel = UserModel.from(entity)

        // then
        assertThat(userModel.id).isEmpty() // publicId가 null이면 빈 문자열로 변환됨
        assertThat(userModel.name).isEqualTo("홍길동")
        assertThat(userModel.phoneNumber).isEqualTo("010-1234-5678")
        assertThat(userModel.birthday).isEqualTo(LocalDate.of(1990, 5, 15))
        assertThat(userModel.calendarType).isEqualTo(CalendarType.SOLAR)
        assertThat(userModel.lunarMonthType).isEqualTo(LunarMonthType.NORMAL)
        assertThat(userModel.teamName).isEqualTo("개발팀")
    }

    @Test
    fun `UserModel from 메서드가 null 값들을 올바르게 처리한다`() {
        // given
        val entity = UserEntity.builder()
            .name("홍길동")
            .build()
        
        // when
        val userModel = UserModel.from(entity)

        // then
        assertThat(userModel.id).isEqualTo("")  // null이면 빈 문자열
        assertThat(userModel.name).isEqualTo("홍길동")
        assertThat(userModel.phoneNumber).isNull()
        assertThat(userModel.birthday).isNull()
        assertThat(userModel.calendarType).isNull()
        assertThat(userModel.lunarMonthType).isEqualTo(LunarMonthType.NORMAL)
        assertThat(userModel.teamName).isNull()
    }

    @Test
    fun `getSolarBirthdayThisYear가 양력 생일을 올바르게 반환한다`() {
        // given
        val userModel = UserModel(
            id = "test-id",
            name = "홍길동",
            phoneNumber = "010-1234-5678",
            teamName = "개발팀",
            birthday = LocalDate.of(1990, 5, 15),
            calendarType = CalendarType.SOLAR,
            lunarMonthType = LunarMonthType.NORMAL
        )

        // when
        val result = userModel.getSolarBirthdayThisYear(2024)

        // then
        assertThat(result).isEqualTo(LocalDate.of(2024, 5, 15))
    }

    @Test
    fun `getSolarBirthdayThisYear가 음력 생일을 양력으로 변환한다`() {
        // given
        val userModel = UserModel(
            id = "test-id",
            name = "홍길동",
            phoneNumber = "010-1234-5678",
            teamName = "개발팀",
            birthday = LocalDate.of(1990, 4, 8), // 음력 4월 8일
            calendarType = CalendarType.LUNAR,
            lunarMonthType = LunarMonthType.NORMAL
        )

        // when
        val result = userModel.getSolarBirthdayThisYear(2024)

        // then
        assertThat(result).isNotNull()
        assertThat(result!!.year).isEqualTo(2024)
    }

    @Test
    fun `getSolarBirthdayThisYear가 생일이나 달력타입이 null일 때 null을 반환한다`() {
        // given
        val userModelWithoutBirthday = UserModel(
            id = "test-id",
            name = "홍길동",
            phoneNumber = "010-1234-5678",
            teamName = "개발팀",
            birthday = null,
            calendarType = CalendarType.SOLAR,
            lunarMonthType = LunarMonthType.NORMAL
        )

        val userModelWithoutCalendarType = UserModel(
            id = "test-id",
            name = "홍길동",
            phoneNumber = "010-1234-5678", 
            teamName = "개발팀",
            birthday = LocalDate.of(1990, 5, 15),
            calendarType = null,
            lunarMonthType = LunarMonthType.NORMAL
        )

        // when & then
        assertThat(userModelWithoutBirthday.getSolarBirthdayThisYear(2024)).isNull()
        assertThat(userModelWithoutCalendarType.getSolarBirthdayThisYear(2024)).isNull()
    }

    @Test
    fun `data class의 equals와 hashCode가 올바르게 동작한다`() {
        // given
        val userModel1 = UserModel(
            id = "test-id",
            name = "홍길동",
            phoneNumber = "010-1234-5678",
            teamName = "개발팀",
            birthday = LocalDate.of(1990, 5, 15),
            calendarType = CalendarType.SOLAR,
            lunarMonthType = LunarMonthType.NORMAL
        )

        val userModel2 = UserModel(
            id = "test-id",
            name = "홍길동",
            phoneNumber = "010-1234-5678",
            teamName = "개발팀",
            birthday = LocalDate.of(1990, 5, 15),
            calendarType = CalendarType.SOLAR,
            lunarMonthType = LunarMonthType.NORMAL
        )

        val userModel3 = UserModel(
            id = "different-id",
            name = "홍길동",
            phoneNumber = "010-1234-5678",
            teamName = "개발팀",
            birthday = LocalDate.of(1990, 5, 15),
            calendarType = CalendarType.SOLAR,
            lunarMonthType = LunarMonthType.NORMAL
        )

        // when & then
        assertThat(userModel1).isEqualTo(userModel2)
        assertThat(userModel1.hashCode()).isEqualTo(userModel2.hashCode())
        assertThat(userModel1).isNotEqualTo(userModel3)
    }
}