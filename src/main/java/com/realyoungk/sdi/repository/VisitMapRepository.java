package com.realyoungk.sdi.repository;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class VisitMapRepository implements VisitRepository {

    @Override
    public Object findByStartedAtAfterOrderByStartedAtDesc(Date startedAt) {
        return null;
    }

    @Override
    public void save(String key, Object value) {
    }
}
