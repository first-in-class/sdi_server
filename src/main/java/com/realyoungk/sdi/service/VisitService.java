package com.realyoungk.sdi.service;

import com.realyoungk.sdi.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VisitService {
    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    /// 다가오는 탐방 일정 조회
    public void getUpComingVisits() {
    }
}
