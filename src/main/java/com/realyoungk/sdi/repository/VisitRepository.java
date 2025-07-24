package com.realyoungk.sdi.repository;


import com.realyoungk.sdi.dto.VisitDto;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitRepository {
    List<VisitDto> findByStartedAtAfterOrderByStartedAtDesc(Date startedAt);

    VisitDto save(VisitDto visitDto);
}