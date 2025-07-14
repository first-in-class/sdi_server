package com.realyoungk.sdi.repository;

import com.realyoungk.sdi.entity.VisitEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface VisitJpaRepository extends JpaRepository<VisitEntity, String>, VisitRepository {
}
