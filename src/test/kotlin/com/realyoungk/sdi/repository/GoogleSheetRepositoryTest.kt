package com.realyoungk.sdi.repository

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Disabled

class GoogleSheetRepositoryTest {

    @Test
    @Disabled("Google Sheets API 의존성으로 인해 비활성화 - 실제 환경에서만 테스트")
    fun `readSheet가 Google Sheets에서 데이터를 읽어온다`() {
        // 이 테스트는 실제 Google Sheets API 호출이 필요하므로
        // 자격 증명 파일과 네트워크 연결이 있는 환경에서만 활성화
        // 단위 테스트는 GoogleSheetRepository의 모킹된 버전을 사용
    }
}