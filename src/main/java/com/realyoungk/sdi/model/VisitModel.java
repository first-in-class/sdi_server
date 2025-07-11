package com.realyoungk.sdi.model;

import lombok.Data;

import java.util.Date;

@Data
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
}
