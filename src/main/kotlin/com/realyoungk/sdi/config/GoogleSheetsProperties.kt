package com.realyoungk.sdi.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * application.yml의 'google.sheets' 경로에 있는 설정값들을 바인딩하는 클래스입니다.
 *
 * @param spreadsheetId 조회할 Google Sheet의 고유 ID
 * @param titleRange    제목을 조회할 범위 (예: "Sheet1!B2:B2")
 * @param dataRange     조회할 데이터의 범위 (예: "Sheet1!C3:G")
 */
@ConfigurationProperties(prefix = "google.sheets")
data class GoogleSheetsProperties(
    val spreadsheetId: String,
    val titleRange: String,
    val dataRange: String
)