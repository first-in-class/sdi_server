package com.realyoungk.sdi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class SdiApplicationTests {

	@Test
	@Disabled("외부 시스템 의존성으로 인해 비활성화 - 통합 테스트에서 별도 관리")
	void contextLoads() {
		// 이 테스트는 실제 Spring Boot 컨텍스트 로딩이 필요하므로
		// 외부 시스템 의존성 없이 실행할 수 있는 환경에서만 활성화
	}

}
