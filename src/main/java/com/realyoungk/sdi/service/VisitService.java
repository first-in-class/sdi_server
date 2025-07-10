package com.realyoungk.sdi.service;

import com.realyoungk.sdi.entity.VisitEntity;
import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class VisitService {
    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    /// 다가오는 탐방 일정 조회
    public void getUpComingVisits() {
    }

    /// 탐방 일정 저장
    public void saveVisit(VisitModel visitModel) {
        VisitEntity entity = new VisitEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        entity.setStartedAt(visitModel.getStartedAt());
        entity.setFinishedAt(visitModel.getFinishedAt());
        entity.setParticipantCount(visitModel.getParticipantCount());
        entity.setTeamName(visitModel.getTeamName());
        entity.setOrganizer(visitModel.getOrganizer());
        entity.setRemark(visitModel.getRemark());
        visitRepository.save(entity.getId(), entity);
    }
}
