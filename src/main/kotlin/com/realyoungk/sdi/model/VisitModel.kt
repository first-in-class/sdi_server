package com.realyoungk.sdi.model

import java.text.SimpleDateFormat
import java.util.*

data class VisitModel(
    val id: String? = null,
    val companyName: String? = null,
    val startedAt: Date? = null,
    val finishedAt: Date? = null,
    val participantCount: String? = null,
    val teamName: String? = null,
    val organizer: String? = null,
    val remark: String? = null
) {
    fun toDetailedString(daysUntil: Long): String {
        val dateFormat = SimpleDateFormat("M월 d일(E)", Locale.KOREAN)
        val sb = StringBuilder()
        sb.append(String.format("‼️ *D-%d* ‼️\n", daysUntil))
        sb.append(String.format("*회사명*: %s\n", this.companyName))
        sb.append(String.format("*일시*: %s\n", startedAt?.let { dateFormat.format(it) }))
        sb.append(String.format("*기수*: %s\n", this.teamName))
        sb.append(String.format("*주선자*: %s\n", this.organizer))
        sb.append(String.format("*참여 인원*: %s\n", this.participantCount))
        if (!remark.isNullOrBlank()) {
            sb.append(String.format("*비고*: %s\n", this.remark))
        }
        return sb.toString()
    }

    fun toSimpleString(daysUtil: Long): String {
        return String.format("🗓️ D-%s / %s / %s %s\n",
                daysUtil,
                this.companyName,
                this.teamName,
                this.organizer)
    }
}