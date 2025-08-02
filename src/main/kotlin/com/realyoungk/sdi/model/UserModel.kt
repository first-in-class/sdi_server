package com.realyoungk.sdi.model

import com.github.fj.koreanlunarcalendar.KoreanLunarCalendarUtils
import com.github.fj.koreanlunarcalendar.KoreanLunarDate
import com.realyoungk.sdi.entity.UserEntity
import com.realyoungk.sdi.enums.CalendarType
import com.realyoungk.sdi.enums.LunarMonthType
import org.slf4j.LoggerFactory
import java.time.LocalDate

data class UserModel(
    val id: String,
    val name: String,
    val phoneNumber: String?,
    val teamName: String?,
    val birthday: LocalDate?,
    val calendarType: CalendarType?,
    val lunarMonthType: LunarMonthType?
) {
    companion object {
        private val log = LoggerFactory.getLogger(UserModel::class.java)
        
        @JvmStatic
        fun from(entity: UserEntity): UserModel {
            return UserModel(
                id = entity.publicId ?: "",
                name = entity.name ?: "",
                phoneNumber = entity.phoneNumber,
                teamName = entity.teamName,
                birthday = entity.birthday,
                calendarType = entity.calendarType,
                lunarMonthType = entity.lunarMonthType
            )
        }
    }

    fun getSolarBirthdayThisYear(year: Int): LocalDate? {
        if (birthday == null || calendarType == null) {
            return null
        }

        return when (calendarType) {
            CalendarType.SOLAR -> birthday.withYear(year)
            CalendarType.LUNAR -> convertLunarToSolarWithApproximation(birthday, year)
        }
    }

    private fun convertLunarToSolarWithApproximation(lunarDate: LocalDate, targetYear: Int): LocalDate? {
        val month = lunarDate.monthValue
        val day = lunarDate.dayOfMonth
        val isLeap = (lunarMonthType == LunarMonthType.LEAP)

        return try {
            val result = KoreanLunarCalendarUtils.getSolarDateOf(targetYear, month, day, isLeap)
            LocalDate.of(result.solYear, result.solMonth, result.solDay)
        } catch (e: Exception) {
            if (day == 30) {
                log.warn("음력 30일 변환 실패, 근사치(29일)로 재시도합니다. lunarDate={}, isLeap={}, targetYear={}", lunarDate, isLeap, targetYear)
                try {
                    val result = KoreanLunarCalendarUtils.getSolarDateOf(targetYear, month, 29, isLeap)
                    LocalDate.of(result.solYear, result.solMonth, result.solDay)
                } catch (e2: Exception) {
                    log.error("음력 변환 최종 실패 (근사치 29일 시도 포함): lunarDate={}, isLeap={}, targetYear={}", lunarDate, isLeap, targetYear, e2)
                    null
                }
            } else {
                log.warn("음력 변환 실패: lunarDate={}, isLeap={}, targetYear={}", lunarDate, isLeap, targetYear, e)
                null
            }
        }
    }
}