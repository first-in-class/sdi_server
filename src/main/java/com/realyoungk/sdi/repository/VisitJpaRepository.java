package com.realyoungk.sdi.repository;

import com.realyoungk.sdi.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitJpaRepository extends JpaRepository<VisitEntity, String>, VisitRepository {
}
