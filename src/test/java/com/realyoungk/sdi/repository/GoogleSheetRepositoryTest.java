package com.realyoungk.sdi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.realyoungk.sdi.config.GoogleSheetsProperties;

@SpringBootTest
class GoogleSheetRepositoryTest {

    @Autowired
    private GoogleSheetRepository googleSheetRepository;
    @Autowired
    private GoogleSheetsProperties googleSheetsProperties;

    @Test
    void readSheetTitle() throws Exception {
        List<List<Object>> result = googleSheetRepository
                .readSheet(googleSheetsProperties.spreadsheetId(), googleSheetsProperties.titleRange());

        assertNotNull(result);
        assertEquals("날짜", result.getFirst().getFirst());
        assertEquals("기업명", result.getFirst().get(1));
        assertEquals("기수", result.getFirst().get(2));
        assertEquals("주선자", result.getFirst().get(3));
        assertEquals("참여 인원", result.getFirst().get(4));
        assertEquals("비고", result.getFirst().get(5));
    }
}

