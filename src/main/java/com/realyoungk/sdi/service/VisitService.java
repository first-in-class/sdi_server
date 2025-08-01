package com.realyoungk.sdi.service;

import com.realyoungk.sdi.config.GoogleSheetsProperties;
import com.realyoungk.sdi.entity.VisitEntity;
import com.realyoungk.sdi.exception.InfrastructureException;
import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.repository.GoogleSheetRepository;
import com.realyoungk.sdi.repository.VisitRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final GoogleSheetRepository googleSheetRepository;
    private final GoogleSheetsProperties googleSheetsProperties;

    public VisitModel save(VisitModel visitModel) {
        final VisitEntity savedVisitEntity = visitRepository.save(VisitEntity.fromModel(visitModel));

        return fromEntity(savedVisitEntity);
    }

    public List<VisitModel> fetchUpcoming() {
        final Date startedAt = Date.from(LocalDateTime
                .now()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        try {
            final List<List<Object>> sheetData = googleSheetRepository.readSheet(
                    googleSheetsProperties.getSpreadsheetId(),
                    googleSheetsProperties.getDataRange()
            );

            if (sheetData == null) return List.of();

            return sheetData.stream()
                    .map(this::fromSheetRow)
                    .filter(Objects::nonNull)
                    .filter(model -> model.getStartedAt() != null && model.getStartedAt().after(startedAt))
                    .sorted(Comparator.comparing(VisitModel::getStartedAt))
                    .toList();
        } catch (IOException | GeneralSecurityException e) {
            log.error("Google Sheet에서 데이터를 가져오는 중 오류가 발생했습니다.", e);
            throw new InfrastructureException.GoogleSheetsUnavailable(e);
        }
    }

    private VisitModel fromEntity(VisitEntity visitEntity) {
        return new VisitModel(
                visitEntity.getId(),
                visitEntity.getCompanyName(), // companyName
                visitEntity.getStartedAt(),
                visitEntity.getFinishedAt(),
                visitEntity.getParticipantCount(),
                visitEntity.getTeamName(),
                visitEntity.getOrganizer(),
                visitEntity.getRemark()
        );
    }

    private VisitModel fromSheetRow(List<Object> row) {
        String startedAtStr = safeToString(row, 0); // B열
        if (startedAtStr.isBlank()) {
            return null;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd");

            return new VisitModel(
                    null, // id
                    safeToString(row, 1),      // companyName (C열)
                    dateFormat.parse(startedAtStr), // startedAt
                    null, // finishedAt
                    safeToString(row, 4),      // participantCount (F열)
                    safeToString(row, 2),      // teamName (D열)
                    safeToString(row, 3),      // organizer (E열)
                    safeToString(row, 5)       // remark (G열)
            );
        } catch (ParseException e) {
            log.warn("Google Sheet의 날짜 형식 파싱에 실패했습니다. Row: {}", row, e);
            return null;
        }
    }

    private String safeToString(List<Object> row, int index) {
        if (row == null || index >= row.size() || row.get(index) == null) {
            return "";
        }
        return row.get(index).toString().trim();
    }
}