package com.realyoungk.sdi.service;

import com.realyoungk.sdi.config.GoogleSheetsProperties;
import com.realyoungk.sdi.entity.VisitEntity;
import com.realyoungk.sdi.exception.VisitFetchException;
import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.repository.GoogleSheetRepository;
import com.realyoungk.sdi.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final GoogleSheetRepository googleSheetRepository;
    private final GoogleSheetsProperties googleSheetsProperties;

    // 다가오는 탐방 일정 조회
    public List<VisitModel> fetchUpcoming(Date startedAt) {
        try {
            final List<List<Object>> sheetData = googleSheetRepository.readSheet(
                    googleSheetsProperties.spreadsheetId(),
                    googleSheetsProperties.dataRange()
            );

            if (sheetData == null) {
                return List.of();
            }

            return sheetData.stream()
                    .map(this::fromSheetRow)
                    .filter(Objects::nonNull)
                    .filter(model -> model.getStartedAt().after(startedAt))
                    .collect(Collectors.toList());

        } catch (IOException | GeneralSecurityException e) {
            log.error("Google Sheet에서 데이터를 가져오는 중 오류가 발생했습니다.", e);
            throw new VisitFetchException("방문 일정을 가져올 수 없습니다. 잠시 후 다시 시도해주세요.", e);
        }
    }

    // 탐방 일정 저장
    public VisitModel save(VisitModel visitModel) {
        final VisitEntity savedVisitEntity = visitRepository.save(VisitEntity.fromModel(visitModel));

        return fromEntity(savedVisitEntity);
    }

    private VisitModel fromEntity(VisitEntity visitEntity) {
        return VisitModel.builder()
                .id(visitEntity.getId())
                .startedAt(visitEntity.getStartedAt())
                .finishedAt(visitEntity.getFinishedAt())
                .participantCount(visitEntity.getParticipantCount())
                .teamName(visitEntity.getTeamName())
                .organizer(visitEntity.getOrganizer())
                .remark(visitEntity.getRemark())
                .build();
    }

    private VisitModel fromSheetRow(List<Object> row) {
        String startedAtStr = safeToString(row, 0); // B열
        if (startedAtStr.isBlank()) {
            return null;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd");

            return VisitModel.builder()
                    .startedAt(dateFormat.parse(startedAtStr))
                    .companyName(safeToString(row, 1))      // C열
                    .teamName(safeToString(row, 2))         // D열
                    .organizer(safeToString(row, 3))        // E열
                    .participantCount(safeToString(row, 4)) // F열
                    .remark(safeToString(row, 5))           // G열 (이제 안전합니다)
                    .build();
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
