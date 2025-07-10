package com.realyoungk.sdi.repository;

import com.realyoungk.sdi.entity.VisitEntity;
import org.hibernate.mapping.List;

import java.util.Date;

public interface VisitRepository {
    Object findByStartedAtAfterOrderByStartedAtDesc(Date startedAt);

    void save(String key, Object value);
}
