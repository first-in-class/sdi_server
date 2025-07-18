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
        if (row == null || row.size() < 6 || row.get(0).toString().isBlank()) return null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            return VisitModel.builder()
                    .startedAt(dateFormat.parse(row.get(0).toString()))
                    .teamName(row.get(2).toString())
                    .organizer(row.get(3).toString())
                    .participantCount(row.get(4).toString())
                    .remark(row.get(5).toString())
                    .build();
        } catch (ParseException | IndexOutOfBoundsException e) {
            log.warn("Google Sheet의 특정 행 파싱에 실패했습니다. Row: {}", row, e);

            return null;
        }
    }
}
