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

@Repository
public class GoogleSheetRepository {
    private static final String APPLICATION_NAME = "sdi-server";
    private static final String CREDENTIALS_FILE_PATH = "firstinvest-google-spread-sheet-key.json";
    public static final String SDI_VISIT_SPREADSHEET_ID = "1jXaJ7gsIMIJi1gfTx96iPi85O9WEJW-np4pE-ItL2l4";
    static final String TITLE_RANGE = "Sheet1!C3:G3";
    public static final String DATA_RANGE = "Sheet1!C3:G";

    private Sheets getSheets() throws IOException, GeneralSecurityException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream())
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));

        return new Sheets
                .Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<List<Object>> readSheet(String spreadsheetId, String range) throws IOException, GeneralSecurityException {
        Sheets sheets = getSheets();
        ValueRange response = sheets.spreadsheets().values().get(spreadsheetId, range).execute();

        return response.getValues();
    }
}
