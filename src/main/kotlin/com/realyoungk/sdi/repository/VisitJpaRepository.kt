package com.realyoungk.sdi.repository

import com.realyoungk.sdi.entity.VisitEntity
import org.springframework.data.jpa.repository.JpaRepository

interface VisitJpaRepository : JpaRepository<VisitEntity, String>, VisitRepository