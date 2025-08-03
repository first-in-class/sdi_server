package com.realyoungk.sdi.repository

import com.realyoungk.sdi.entity.VisitEntity
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VisitRepository {
    fun findByStartedAtAfterOrderByStartedAtDesc(startedAt: Date): List<VisitEntity>
    
    fun save(visitEntity: VisitEntity): VisitEntity
}