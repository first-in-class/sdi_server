package com.realyoungk.sdi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.scheduling.annotation.EnableScheduling

class SdiApplicationTest {

    @Test
    fun `SdiApplication 클래스 존재 확인 테스트`() {
        // Given & When
        val applicationClass = SdiApplication::class.java
        
        // Then
        assertNotNull(applicationClass)
        assertTrue(applicationClass.isAnnotationPresent(SpringBootApplication::class.java))
        assertTrue(applicationClass.isAnnotationPresent(ConfigurationPropertiesScan::class.java))
        assertTrue(applicationClass.isAnnotationPresent(EnableScheduling::class.java))
    }

    @Test
    fun `main 함수 존재 확인 테스트`() {
        // Given & When
        // Kotlin 파일의 main 함수는 컴파일된 클래스에서 확인 가능
        val kotlinFile = "com.realyoungk.sdi.SdiApplicationKt"
        val mainMethod = try {
            Class.forName(kotlinFile).getMethod("main", Array<String>::class.java)
        } catch (e: Exception) {
            null
        }
        
        // Then
        assertNotNull(mainMethod, "main 함수가 존재해야 합니다")
    }

    @Test
    fun `SdiApplication 클래스가 Kotlin 클래스인지 확인 테스트`() {
        // Given & When
        val applicationClass = SdiApplication::class.java
        
        // Then
        // Kotlin 클래스는 Metadata 어노테이션을 가집니다
        assertTrue(applicationClass.isAnnotationPresent(kotlin.Metadata::class.java))
    }
}