package com.realyoungk.sdi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
// 탐방
public class VisitModel {
    private String id;
    // 시작시각
    private Date startedAt;
    // 끝나는시각
    private Date finishedAt;
    // 참여인원
    private String participantCount;
    // 기수
    private String teamName;
    // 주선자
    private String organizer;
    // 비고
    private String remark;

    @Builder
    public VisitModel(String id, Date startedAt, Date finishedAt, String participantCount, String teamName, String organizer, String remark) {
        this.id = id;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.participantCount = participantCount;
        this.teamName = teamName;
        this.organizer = organizer;
        this.remark = remark;
    }
}
