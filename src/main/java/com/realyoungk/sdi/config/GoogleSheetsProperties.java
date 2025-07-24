package com.realyoungk.sdi.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * application.yml의 'google.sheets' 경로에 있는 설정값들을 바인딩하는 클래스입니다.
 *
 * @param spreadsheetId 조회할 Google Sheet의 고유 ID
 * @param dataRange     조회할 데이터의 범위 (예: "Sheet1!C3:G")
 */
@ConfigurationProperties(prefix = "google.sheets")
public record GoogleSheetsProperties(String spreadsheetId, String titleRange, String dataRange) {
}