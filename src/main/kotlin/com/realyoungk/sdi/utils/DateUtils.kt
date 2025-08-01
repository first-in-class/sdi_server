package com.realyoungk.sdi.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    private val formatter = DateTimeFormatter.ofPattern("MM월 dd일")
    
    fun formatBirthday(date: LocalDate?): String {
        return date?.format(formatter) ?: "정보 없음"
    }
    
    fun isValidBirthday(date: LocalDate?): Boolean {
        if (date == null) return false
        val now = LocalDate.now()
        return date.isBefore(now) && date.isAfter(now.minusYears(150))
    }
}