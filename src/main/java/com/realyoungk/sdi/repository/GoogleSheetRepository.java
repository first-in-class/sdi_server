package com.realyoungk.sdi.repository;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.auth.http.HttpCredentialsAdapter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * [Google Sheets API 사용 한도]
 * - 읽기 요청(프로젝트 기준): 분당 300회
 * - 쓰기 요청(프로젝트 기준): 분당 60~100회 (공식 문서에 명확한 한도 미기재)
 * - 사용자별 호출 한도: 100초 당 100회
 * - 프로젝트별 100초 한도: 100초 당 500회
 * - 패이로드 권장 최대: 2MB
 * 참고: 할당량 초과 시 429 에러(Too Many Requests)가 발생하며, 대기 후 재시도 필요.
 * Google Workspace 등 유료 계정은 한도 상향 신청 가능.
 * 자세한 쿼터는 Google Cloud Console에서 확인 및 조정 가능.
 */
@Repository
public class GoogleSheetRepository {
    private static final String APPLICATION_NAME = "sdi-server";
    private static final String CREDENTIALS_FILE_PATH = "firstinvest-google-spread-sheet-key.json";

    public List<List<Object>> readSheet(String spreadsheetId, String range) throws IOException, GeneralSecurityException {
        Sheets sheets = getSheets();
        ValueRange response = sheets.spreadsheets().values().get(spreadsheetId, range).execute();

        return response.getValues();
    }

    private Sheets getSheets() throws IOException, GeneralSecurityException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream())
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));

        return new Sheets
                .Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
