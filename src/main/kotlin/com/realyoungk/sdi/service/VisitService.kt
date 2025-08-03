package com.realyoungk.sdi.service

import com.realyoungk.sdi.config.GoogleSheetsProperties
import com.realyoungk.sdi.entity.VisitEntity
import com.realyoungk.sdi.exception.InfrastructureException
import com.realyoungk.sdi.model.VisitModel
import com.realyoungk.sdi.repository.GoogleSheetRepository
import com.realyoungk.sdi.repository.VisitRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.IOException
import java.security.GeneralSecurityException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class VisitService(
    private val visitRepository: VisitRepository,
    private val googleSheetRepository: GoogleSheetRepository,
    private val googleSheetsProperties: GoogleSheetsProperties
) {
    private val log = LoggerFactory.getLogger(VisitService::class.java)

    fun save(visitModel: VisitModel): VisitModel {
        val savedVisitEntity = visitRepository.save(VisitEntity.fromModel(visitModel))
        return fromEntity(savedVisitEntity)
    }

    fun fetchUpcoming(): List<VisitModel> {
        val startedAt = Date.from(
            LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .toInstant()
        )

        return try {
            val sheetData = googleSheetRepository.readSheet(
                googleSheetsProperties.spreadsheetId,
                googleSheetsProperties.dataRange
            )

            if (sheetData == null) return emptyList()

            sheetData.mapNotNull { row -> fromSheetRow(row) }
                .filter { model -> model.startedAt != null && model.startedAt.after(startedAt) }
                .sortedBy { model -> model.startedAt }
        } catch (e: IOException) {
            log.error("Google Sheet에서 데이터를 가져오는 중 오류가 발생했습니다.", e)
            throw InfrastructureException.GoogleSheetsUnavailable(e)
        } catch (e: GeneralSecurityException) {
            log.error("Google Sheet에서 데이터를 가져오는 중 오류가 발생했습니다.", e)
            throw InfrastructureException.GoogleSheetsUnavailable(e)
        }
    }

    private fun fromEntity(visitEntity: VisitEntity): VisitModel {
        return VisitModel(
            id = visitEntity.id,
            companyName = visitEntity.companyName,
            startedAt = visitEntity.startedAt,
            finishedAt = visitEntity.finishedAt,
            participantCount = visitEntity.participantCount,
            teamName = visitEntity.teamName,
            organizer = visitEntity.organizer,
            remark = visitEntity.remark
        )
    }

    private fun fromSheetRow(row: List<Any>): VisitModel? {
        val startedAtStr = safeToString(row, 0) // B열
        if (startedAtStr.isBlank()) {
            return null
        }

        return try {
            val dateFormat = SimpleDateFormat("yy.MM.dd")

            VisitModel(
                id = null,
                companyName = safeToString(row, 1),      // companyName (C열)
                startedAt = dateFormat.parse(startedAtStr), // startedAt
                finishedAt = null,
                participantCount = safeToString(row, 4),  // participantCount (F열)
                teamName = safeToString(row, 2),         // teamName (D열)
                organizer = safeToString(row, 3),        // organizer (E열)
                remark = safeToString(row, 5)            // remark (G열)
            )
        } catch (e: ParseException) {
            log.warn("Google Sheet의 날짜 형식 파싱에 실패했습니다. Row: {}", row, e)
            null
        }
    }

    private fun safeToString(row: List<Any>?, index: Int): String {
        if (row == null || index >= row.size) {
            return ""
        }
        return row[index].toString().trim()
    }
}