package com.realyoungk.sdi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
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

    public String toDetailedString(long daysUntil) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M월 d일(E)");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("‼️ *D-%d* ‼️\n", daysUntil));
        sb.append(String.format("*일시*: %s\n", dateFormat.format(this.startedAt)));
        sb.append(String.format("*기수*: %s\n", this.teamName));
        sb.append(String.format("*주선자*: %s\n", this.organizer));
        sb.append(String.format("*참여 인원*: %s\n", this.participantCount));
        if (this.remark != null && !this.remark.isBlank()) {
            sb.append(String.format("*비고*: %s\n", this.remark));
        }
        sb.append("--------------------");
        return sb.toString();
    }

    public String toSimpleString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M월 d일(E)");
        return String.format("🗓️ %s - %s (%s 주선)",
                dateFormat.format(this.startedAt),
                this.teamName,
                this.organizer);
    }
}
