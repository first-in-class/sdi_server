package com.realyoungk.sdi.repository;


import com.realyoungk.sdi.entity.VisitEntity;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitRepository {
    List<VisitEntity> findByStartedAtAfterOrderByStartedAtDesc(Date startedAt);

    VisitEntity save(VisitEntity visitEntity);
}