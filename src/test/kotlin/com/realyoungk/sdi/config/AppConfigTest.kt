package com.realyoungk.sdi.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestTemplate

class AppConfigTest {

    @Test
    fun `AppConfig가 Configuration 어노테이션을 가지는지 테스트`() {
        // Given & When
        val configClass = AppConfig::class.java
        
        // Then
        assertTrue(configClass.isAnnotationPresent(org.springframework.context.annotation.Configuration::class.java))
    }

    @Test
    fun `RestTemplate Bean이 올바르게 생성되는지 테스트`() {
        // Given
        val appConfig = AppConfig()
        
        // When
        val restTemplate = appConfig.restTemplate()
        
        // Then
        assertNotNull(restTemplate)
        assertTrue(restTemplate is RestTemplate)
    }

    @Test
    fun `RestTemplate Bean 메서드가 Bean 어노테이션을 가지는지 테스트`() {
        // Given & When
        val method = AppConfig::class.java.getMethod("restTemplate")
        
        // Then
        assertTrue(method.isAnnotationPresent(org.springframework.context.annotation.Bean::class.java))
    }

    @Test
    fun `직접 메서드 호출 시 새로운 RestTemplate 인스턴스가 생성되는지 테스트`() {
        // Given
        val appConfig = AppConfig()
        
        // When
        val restTemplate1 = appConfig.restTemplate()
        val restTemplate2 = appConfig.restTemplate()
        
        // Then
        // Spring 컨테이너 없이 직접 호출하면 매번 새 인스턴스가 생성됨
        assertNotSame(restTemplate1, restTemplate2)
        
        // 하지만 Spring 컨테이너에서는 @Bean으로 인해 싱글톤으로 관리됨을 명시
        // (실제 Spring 컨테이너 테스트는 통합 테스트에서 확인)
    }
}

