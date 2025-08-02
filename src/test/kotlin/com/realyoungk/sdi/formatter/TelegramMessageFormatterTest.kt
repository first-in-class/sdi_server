package com.realyoungk.sdi.formatter

import com.realyoungk.sdi.enums.CalendarType
import com.realyoungk.sdi.enums.LunarMonthType
import com.realyoungk.sdi.model.UserModel
import com.realyoungk.sdi.model.VisitModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class TelegramMessageFormatterTest {

    private lateinit var formatter: TelegramMessageFormatter

    @BeforeEach
    fun setUp() {
        formatter = TelegramMessageFormatter()
    }

    @Test
    fun `formatVisitsMessage가 빈 목록일 때 적절한 메시지를 반환한다`() {
        // given
        val emptyList = emptyList<VisitModel>()

        // when
        val result = formatter.formatVisitsMessage(emptyList)

        // then
        assertThat(result).isEqualTo("다가오는 탐방 일정이 없습니다. 😅")
    }

    @Test
    fun `formatVisitsMessage가 null일 때 적절한 메시지를 반환한다`() {
        // when
        val result = formatter.formatVisitsMessage(null)

        // then
        assertThat(result).isEqualTo("다가오는 탐방 일정이 없습니다. 😅")
    }

    @Test
    fun `formatVisitsMessage가 방문 일정 목록을 올바르게 포맷한다`() {
        // given
        val tomorrow = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant())
        val nextWeek = Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant())
        
        val visitModels = listOf(
            VisitModel(
                id = "1",
                companyName = "삼성전자",
                startedAt = tomorrow,
                finishedAt = null,
                participantCount = "10명",
                teamName = "개발팀",
                organizer = "홍길동",
                remark = "중요한 미팅"
            ),
            VisitModel(
                id = "2", 
                companyName = "LG전자",
                startedAt = nextWeek,
                finishedAt = null,
                participantCount = "5명",
                teamName = "기획팀",
                organizer = "김철수",
                remark = null
            )
        )

        // when
        val result = formatter.formatVisitsMessage(visitModels)

        // then
        assertThat(result).contains("📢 [다가오는 탐방 일정]")
        assertThat(result).contains("삼성전자")
        assertThat(result).contains("LG전자")
        assertThat(result).contains("홍길동")
        assertThat(result).contains("김철수")
    }

    @Test
    fun `formatVisitsMessage가 startedAt이 null인 방문을 건너뛴다`() {
        // given
        val visitModels = listOf(
            VisitModel(
                id = "1",
                companyName = "삼성전자",
                startedAt = null, // null인 경우
                finishedAt = null,
                participantCount = "10명",
                teamName = "개발팀",
                organizer = "홍길동",
                remark = "중요한 미팅"
            )
        )

        // when
        val result = formatter.formatVisitsMessage(visitModels)

        // then
        assertThat(result).isEqualTo("📢 [다가오는 탐방 일정]\n\n")
    }

    @Test
    fun `formatBirthdayMessage가 빈 목록일 때 적절한 메시지를 반환한다`() {
        // given
        val emptyList = emptyList<UserModel>()
        val period = "오늘"

        // when
        val result = formatter.formatBirthdayMessage(emptyList, period)

        // then
        assertThat(result).isEqualTo("오늘 생일인 분이 없습니다. 🎂")
    }

    @Test
    fun `formatBirthdayMessage가 null일 때 적절한 메시지를 반환한다`() {
        // given
        val period = "이번 달"

        // when
        val result = formatter.formatBirthdayMessage(null, period)

        // then
        assertThat(result).isEqualTo("이번 달 생일인 분이 없습니다. 🎂")
    }

    @Test
    fun `formatBirthdayMessage가 생일자 목록을 올바르게 포맷한다`() {
        // given
        val users = listOf(
            UserModel(
                id = "1",
                name = "홍길동",
                phoneNumber = "010-1234-5678",
                teamName = "개발팀",
                birthday = LocalDate.of(1990, 5, 15),
                calendarType = CalendarType.SOLAR,
                lunarMonthType = LunarMonthType.NORMAL
            ),
            UserModel(
                id = "2",
                name = "김철수",
                phoneNumber = "010-9876-5432",
                teamName = "기획팀",
                birthday = LocalDate.of(1985, 3, 22),
                calendarType = CalendarType.SOLAR,
                lunarMonthType = LunarMonthType.NORMAL
            )
        )
        val period = "오늘"

        // when
        val result = formatter.formatBirthdayMessage(users, period)

        // then
        assertThat(result).contains("🎉 오늘 생일인 분들입니다! 🎉")
        assertThat(result).contains("홍길동님")
        assertThat(result).contains("김철수님")
        assertThat(result).contains("모두 축하해주세요! 🥳")
    }

    @Test
    fun `formatBirthdayMessage가 생일이 null인 사용자도 처리한다`() {
        // given
        val users = listOf(
            UserModel(
                id = "1",
                name = "홍길동",
                phoneNumber = "010-1234-5678",
                teamName = "개발팀",
                birthday = null, // 생일이 null
                calendarType = CalendarType.SOLAR,
                lunarMonthType = LunarMonthType.NORMAL
            )
        )
        val period = "오늘"

        // when
        val result = formatter.formatBirthdayMessage(users, period)

        // then
        assertThat(result).contains("홍길동님")
        assertThat(result).contains("- 홍길동님\n") // 날짜 없이 이름만 표시됨
        assertThat(result).doesNotContain("홍길동님 (")
    }

    @Test
    fun `formatBirthdayMessage가 음력 생일을 양력으로 변환하여 표시한다`() {
        // given
        val users = listOf(
            UserModel(
                id = "1",
                name = "홍길동",
                phoneNumber = "010-1234-5678",
                teamName = "개발팀",
                birthday = LocalDate.of(1990, 4, 8), // 음력 4월 8일
                calendarType = CalendarType.LUNAR,
                lunarMonthType = LunarMonthType.NORMAL
            )
        )
        val period = "오늘"

        // when
        val result = formatter.formatBirthdayMessage(users, period)

        // then
        assertThat(result).contains("홍길동님")
        // 음력이 양력으로 변환되어 표시되어야 함
        assertThat(result).contains("월")
        assertThat(result).contains("일")
    }
}