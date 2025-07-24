package com.realyoungk.sdi.entity;

import com.realyoungk.sdi.model.VisitModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Getter
@ToString(of = {"id", "teamName", "organizer", "startedAt"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private VisitEntity(String id, Date createdAt, Date updatedAt, Date startedAt, Date finishedAt, String participantCount, String teamName, String organizer, String remark) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.participantCount = participantCount;
        this.teamName = teamName;
        this.organizer = organizer;
        this.remark = remark;
    }

    public static VisitEntity fromModel(VisitModel model) {
        return VisitEntity.builder()
                .id(UUID.randomUUID().toString())
                .createdAt(new Date())
                .updatedAt(new Date())
                .startedAt(model.getStartedAt())
                .finishedAt(model.getFinishedAt())
                .participantCount(model.getParticipantCount())
                .teamName(model.getTeamName())
                .organizer(model.getOrganizer())
                .remark(model.getRemark())
                .build();
    }
}
