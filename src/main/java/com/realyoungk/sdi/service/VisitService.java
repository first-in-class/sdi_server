package com.realyoungk.sdi.service;

import com.realyoungk.sdi.entity.VisitEntity;
import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VisitService {
    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    // 다가오는 탐방 일정 조회
    public List<VisitModel> getUpcoming(Date startedAt) {
        return visitRepository.findByStartedAtAfterOrderByStartedAtDesc(startedAt)
                .stream()
                .map(this::fromEntity)
                .toList();
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
}
