package com.realyoungk.sdi.model;

import java.util.Date;

/// 탐방
public class VisitModel {
    /// 시작시각
    private Date startedAt;
    /// 끝나는시각
    private Date finishedAt;
    /// 참여인원
    private String participantCount;
    /// 기수
    private String teamName;
    /// 주선자
    private String organizer;
    /// 비고
    private String remark;

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(String participantCount) {
        this.participantCount = participantCount;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
