package com.realyoungk.sdi.repository;

import com.realyoungk.sdi.entity.VisitEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Primary
public interface VisitJpaRepository extends JpaRepository<VisitEntity, String>, VisitRepository {
}
