package com.realyoungk.sdi.entity;

import com.realyoungk.sdi.model.VisitModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class VisitEntity {
    @Id
    private String id;
    private Date createdAt;
    private Date updatedAt;

    // 시작시각
    private Date startedAt;
    // 끝나는시각
    private Date finishedAt;
    // 참여자 수: ex. 10, 13, 무제한, null
    private String participantCount;
    // 기수: ex. 1기, 2기, 3기, 강남지스
    private String teamName;
    // 주선자: ex. 김영우, 이지은
    private String organizer;
    // 비고
    private String remark;

    public static VisitEntity fromModel(VisitModel model) {
        VisitEntity entity = new VisitEntity();
        entity.id = UUID.randomUUID().toString();
        entity.createdAt = new Date();
        entity.updatedAt = new Date();
        entity.startedAt = model.getStartedAt();
        entity.finishedAt = model.getFinishedAt();
        entity.participantCount = model.getParticipantCount();
        entity.teamName = model.getTeamName();
        entity.organizer = model.getOrganizer();
        entity.remark = model.getRemark();

        return entity;
    }
}
