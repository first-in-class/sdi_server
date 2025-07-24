package com.realyoungk.sdi.repository;

import com.realyoungk.sdi.dto.VisitDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitJpaRepository extends JpaRepository<VisitDto, String>, VisitRepository {
}
