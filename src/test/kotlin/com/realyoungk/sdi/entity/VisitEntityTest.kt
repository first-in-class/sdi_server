package com.realyoungk.sdi.entity

import com.realyoungk.sdi.model.VisitModel
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class VisitEntityTest {

    @Test
    fun `VisitEntity가 JPA Entity 어노테이션을 가지는지 테스트`() {
        // Given & When
        val entityClass = VisitEntity::class.java

        // Then
        assertTrue(entityClass.isAnnotationPresent(Entity::class.java))
    }

    @Test
    fun `VisitEntity 데이터 클래스가 올바르게 작동하는지 테스트`() {
        // Given
        val id = "test-id"
        val createdAt = Date()
        val updatedAt = Date()
        val startedAt = Date()
        val finishedAt = Date()
        val companyName = "테스트 회사"
        val participantCount = "10명"
        val teamName = "1기"
        val organizer = "김영우"
        val remark = "테스트 비고"

        // When
        val visitEntity = VisitEntity(
            id = id,
            createdAt = createdAt,
            updatedAt = updatedAt,
            startedAt = startedAt,
            finishedAt = finishedAt,
            companyName = companyName,
            participantCount = participantCount,
            teamName = teamName,
            organizer = organizer,
            remark = remark
        )

        // Then
        assertEquals(id, visitEntity.id)
        assertEquals(createdAt, visitEntity.createdAt)
        assertEquals(updatedAt, visitEntity.updatedAt)
        assertEquals(startedAt, visitEntity.startedAt)
        assertEquals(finishedAt, visitEntity.finishedAt)
        assertEquals(companyName, visitEntity.companyName)
        assertEquals(participantCount, visitEntity.participantCount)
        assertEquals(teamName, visitEntity.teamName)
        assertEquals(organizer, visitEntity.organizer)
        assertEquals(remark, visitEntity.remark)
    }

    @Test
    fun `VisitModel로부터 VisitEntity 생성이 올바르게 작동하는지 테스트`() {
        // Given
        val startedAt = Date()
        val finishedAt = Date()
        val participantCount = "15명"
        val teamName = "2기"
        val organizer = "이지은"
        val remark = "모델 테스트"

        val visitModel = VisitModel(
            startedAt = startedAt,
            finishedAt = finishedAt,
            participantCount = participantCount,
            teamName = teamName,
            organizer = organizer,
            remark = remark
        )

        // When
        val visitEntity = VisitEntity.fromModel(visitModel)

        // Then
        assertNotNull(visitEntity.id) // UUID가 자동 생성됨
        assertNotNull(visitEntity.createdAt) // 현재 시간으로 설정됨
        assertNotNull(visitEntity.updatedAt) // 현재 시간으로 설정됨
        assertEquals(startedAt, visitEntity.startedAt)
        assertEquals(finishedAt, visitEntity.finishedAt)
        assertEquals(visitModel.companyName, visitEntity.companyName)
        assertEquals(participantCount, visitEntity.participantCount)
        assertEquals(teamName, visitEntity.teamName)
        assertEquals(organizer, visitEntity.organizer)
        assertEquals(remark, visitEntity.remark)
    }

    @Test
    fun `VisitEntity toString 메서드가 올바르게 작동하는지 테스트`() {
        // Given
        val visitEntity = VisitEntity(
            id = "test-id",
            createdAt = Date(),
            updatedAt = Date(),
            startedAt = Date(),
            finishedAt = null,
            companyName = "테스트 회사",
            participantCount = null,
            teamName = "1기",
            organizer = "김영우",
            remark = null
        )

        // When
        val toString = visitEntity.toString()

        // Then
        assertTrue(toString.contains("test-id"))
        assertTrue(toString.contains("1기"))
        assertTrue(toString.contains("김영우"))
        assertTrue(toString.contains("VisitEntity"))
    }

    @Test
    fun `VisitEntity equals와 hashCode가 data class로 작동하는지 테스트`() {
        // Given
        val baseDate = Date()
        
        val entity1 = VisitEntity(
            id = "test-id-1",
            createdAt = baseDate,
            updatedAt = baseDate,
            startedAt = baseDate,
            finishedAt = null,
            companyName = "회사1",
            participantCount = "10명",
            teamName = "1기",
            organizer = "김영우",
            remark = null
        )
            
        val entity2 = VisitEntity(
            id = "test-id-1", // 같은 모든 필드
            createdAt = baseDate,
            updatedAt = baseDate,
            startedAt = baseDate,
            finishedAt = null,
            companyName = "회사1",
            participantCount = "10명",
            teamName = "1기",
            organizer = "김영우",
            remark = null
        )
            
        val entity3 = VisitEntity(
            id = "test-id-2", // 다른 ID
            createdAt = baseDate,
            updatedAt = baseDate,
            startedAt = baseDate,
            finishedAt = null,
            companyName = "회사1",
            participantCount = "10명",
            teamName = "1기",
            organizer = "김영우",
            remark = null
        )

        // When & Then
        assertEquals(entity1, entity2) // 모든 필드가 같으므로 equal
        assertNotEquals(entity1, entity3) // id가 다르므로 not equal
        assertEquals(entity1.hashCode(), entity2.hashCode()) // 모든 필드가 같으므로 hashCode도 같음
    }

    @Test
    fun `JPA ID 필드 어노테이션이 올바르게 적용되었는지 테스트`() {
        // Given & When
        val entityClass = VisitEntity::class.java
        val idField = entityClass.getDeclaredField("id")

        // Then
        assertTrue(idField.isAnnotationPresent(Id::class.java))
    }

    @Test
    fun `기본 생성자가 존재하는지 테스트`() {
        // Given & When & Then
        assertDoesNotThrow {
            // private 생성자이므로 reflection을 통해 접근
            val constructor = VisitEntity::class.java.getDeclaredConstructor()
            constructor.isAccessible = true
            val entity = constructor.newInstance()
            assertNotNull(entity)
            assertEquals("", entity.id)
        }
    }
}