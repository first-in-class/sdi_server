package com.realyoungk.sdi.entity

import com.realyoungk.sdi.enums.CalendarType
import com.realyoungk.sdi.enums.LunarMonthType
import jakarta.persistence.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class UserEntityTest {

    @Test
    fun `UserEntity가 JPA Entity 어노테이션을 가지는지 테스트`() {
        // Given & When
        val entityClass = UserEntity::class.java

        // Then
        assertTrue(entityClass.isAnnotationPresent(Entity::class.java))
        assertTrue(entityClass.isAnnotationPresent(Table::class.java))
        
        val tableAnnotation = entityClass.getAnnotation(Table::class.java)
        assertEquals("users", tableAnnotation.name)
    }

    @Test
    fun `UserEntity Builder 패턴이 올바르게 작동하는지 테스트`() {
        // Given
        val name = "테스트 사용자"
        val phoneNumber = "010-1234-5678"
        val birthday = LocalDate.of(1990, 1, 15)
        val calendarType = CalendarType.LUNAR
        val lunarMonthType = LunarMonthType.LEAP
        val teamName = "테스트팀"

        // When
        val userEntity = UserEntity.builder()
            .name(name)
            .phoneNumber(phoneNumber)
            .birthday(birthday)
            .calendarType(calendarType)
            .lunarMonthType(lunarMonthType)
            .teamName(teamName)
            .build()

        // Then
        assertEquals(name, userEntity.name)
        assertEquals(phoneNumber, userEntity.phoneNumber)
        assertEquals(birthday, userEntity.birthday)
        assertEquals(calendarType, userEntity.calendarType)
        assertEquals(lunarMonthType, userEntity.lunarMonthType)
        assertEquals(teamName, userEntity.teamName)
        assertNull(userEntity.id) // 아직 persist되지 않았으므로 null
        assertNull(userEntity.publicId) // onCreate가 호출되지 않았으므로 null
    }

    @Test
    fun `태양력 설정 시 lunarMonthType이 NORMAL로 설정되는지 테스트`() {
        // Given & When
        val userEntity = UserEntity.builder()
            .name("테스트 사용자")
            .calendarType(CalendarType.SOLAR)
            .lunarMonthType(LunarMonthType.LEAP) // 의도적으로 LEAP 설정
            .build()

        // Then
        assertEquals(CalendarType.SOLAR, userEntity.calendarType)
        assertEquals(LunarMonthType.NORMAL, userEntity.lunarMonthType) // SOLAR일 때는 강제로 NORMAL
    }

    @Test
    fun `음력 설정 시 지정한 lunarMonthType이 유지되는지 테스트`() {
        // Given & When
        val userEntity = UserEntity.builder()
            .name("테스트 사용자")
            .calendarType(CalendarType.LUNAR)
            .lunarMonthType(LunarMonthType.LEAP)
            .build()

        // Then
        assertEquals(CalendarType.LUNAR, userEntity.calendarType)
        assertEquals(LunarMonthType.LEAP, userEntity.lunarMonthType) // LUNAR일 때는 지정한 값 유지
    }

    @Test
    fun `JPA 필드 어노테이션이 올바르게 적용되었는지 테스트`() {
        // Given & When
        val entityClass = UserEntity::class.java
        val idField = entityClass.getDeclaredField("id")
        val publicIdField = entityClass.getDeclaredField("publicId")
        val nameField = entityClass.getDeclaredField("name")
        val calendarTypeField = entityClass.getDeclaredField("calendarType")
        val lunarMonthTypeField = entityClass.getDeclaredField("lunarMonthType")

        // Then
        // ID 필드 검증
        assertTrue(idField.isAnnotationPresent(Id::class.java))
        assertTrue(idField.isAnnotationPresent(GeneratedValue::class.java))
        
        // PublicId 필드 검증
        assertTrue(publicIdField.isAnnotationPresent(Column::class.java))
        val publicIdColumn = publicIdField.getAnnotation(Column::class.java)
        assertFalse(publicIdColumn.nullable)
        assertTrue(publicIdColumn.unique)
        assertFalse(publicIdColumn.updatable)
        
        // Name 필드 검증
        assertTrue(nameField.isAnnotationPresent(Column::class.java))
        val nameColumn = nameField.getAnnotation(Column::class.java)
        assertFalse(nameColumn.nullable)
        assertEquals(50, nameColumn.length)
        
        // Enum 필드들 검증
        assertTrue(calendarTypeField.isAnnotationPresent(Enumerated::class.java))
        assertTrue(lunarMonthTypeField.isAnnotationPresent(Enumerated::class.java))
        
        val lunarMonthTypeColumn = lunarMonthTypeField.getAnnotation(Column::class.java)
        assertFalse(lunarMonthTypeColumn.nullable)
    }

    @Test
    fun `기본 생성자가 존재하는지 테스트`() {
        // Given & When & Then
        assertDoesNotThrow {
            // protected 생성자이므로 reflection을 통해 접근
            val constructor = UserEntity::class.java.getDeclaredConstructor()
            constructor.isAccessible = true
            val entity = constructor.newInstance()
            assertNotNull(entity)
        }
    }
}