package com.realyoungk.sdi.entity

import com.realyoungk.sdi.model.VisitModel
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.Date
import java.util.UUID

@Entity
data class VisitEntity(
    @Id
    val id: String,
    val createdAt: Date,
    val updatedAt: Date,

    // 시작시각
    val startedAt: Date?,

    // 끝나는시각
    val finishedAt: Date?,

    val companyName: String?,

    // 참여자 수: ex. 10, 13, 무제한, null
    val participantCount: String?,

    // 기수: ex. 1기, 2기, 3기, 강남지스
    val teamName: String?,

    // 주선자: ex. 김영우, 이지은
    val organizer: String?,

    // 비고
    val remark: String?
) {
    companion object {
        @JvmStatic
        fun fromModel(model: VisitModel): VisitEntity {
            return VisitEntity(
                id = UUID.randomUUID().toString(),
                createdAt = Date(),
                updatedAt = Date(),
                startedAt = model.startedAt,
                finishedAt = model.finishedAt,
                companyName = model.companyName,
                participantCount = model.participantCount,
                teamName = model.teamName,
                organizer = model.organizer,
                remark = model.remark
            )
        }
    }

    // 롬복 @NoArgsConstructor(access = AccessLevel.PROTECTED) 대체
    private constructor() : this(
        id = "",
        createdAt = Date(),
        updatedAt = Date(),
        startedAt = null,
        finishedAt = null,
        companyName = null,
        participantCount = null,
        teamName = null,
        organizer = null,
        remark = null
    )
}
