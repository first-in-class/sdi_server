package com.realyoungk.sdi.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    private val koreanMonthDayFormatter = DateTimeFormatter.ofPattern("MM월 dd일")
    
    /**
     * 날짜를 한국어 월일 형식으로 포맷팅합니다.
     * @param date 포맷팅할 날짜
     * @param defaultMessage 날짜가 null일 때 반환할 기본 메시지
     * @return 포맷팅된 문자열 (예: "12월 25일") 또는 기본 메시지
     */
    fun formatToKoreanMonthDay(date: LocalDate?, defaultMessage: String = "정보 없음"): String {
        return date?.format(koreanMonthDayFormatter) ?: defaultMessage
    }
    
    /**
     * 날짜가 지정된 범위 내에 있는지 확인합니다.
     * @param date 확인할 날짜
     * @param maxYearsAgo 과거 최대 연수 (기본: 150년)
     * @param allowToday 오늘 날짜 허용 여부 (기본: false)
     * @return 유효한 날짜인지 여부
     */
    fun isDateInValidRange(
        date: LocalDate?, 
        maxYearsAgo: Long = 150, 
        allowToday: Boolean = false
    ): Boolean {
        if (date == null) return false
        
        val now = LocalDate.now()
        val maxPastDate = now.minusYears(maxYearsAgo)
        
        val isNotTooOld = date.isAfter(maxPastDate)
        val isNotFuture = if (allowToday) !date.isAfter(now) else date.isBefore(now)
        
        return isNotTooOld && isNotFuture
    }
    
    /**
     * 날짜가 과거인지 확인합니다.
     * @param date 확인할 날짜
     * @param includeToday 오늘 포함 여부 (기본: false)
     * @return 과거 날짜인지 여부
     */
    fun isPastDate(date: LocalDate?, includeToday: Boolean = false): Boolean {
        if (date == null) return false
        val now = LocalDate.now()
        return if (includeToday) !date.isAfter(now) else date.isBefore(now)
    }
}