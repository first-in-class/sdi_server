package com.realyoungk.sdi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class VisitEntity {
    @Id
    private String id;
    private Date createdAt;
    private Date updatedAt;

    /// 시작시각
    private Date startedAt;
    /// 끝나는시각
    private Date finishedAt;
    /// 참여자 수: ex. 10, 13, 무제한, null
    private String participantCount;
    /// 기수: ex. 1기, 2기, 3기, 강남지스
    private String teamName;
    /// 주선자: ex. 김영우, 이지은
    private String organizer;
    /// 비고
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

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
