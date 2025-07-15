package com.realyoungk.sdi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoogleSheetRepositoryTest {

    @Autowired
    private GoogleSheetRepository googleSheetRepository;


    @Test
    void readSheetTitle() throws Exception {
        final String SPREADSHEET_ID = "1jXaJ7gsIMIJi1gfTx96iPi85O9WEJW-np4pE-ItL2l4";
        final String RANGE = "Sheet1!C3:G3";

        List<List<Object>> result = googleSheetRepository.readSheet(SPREADSHEET_ID, RANGE);

        assertNotNull(result);
        assertEquals("기업명", result.getFirst().getFirst());
        assertEquals("기수", result.getFirst().get(1));
        assertEquals("주선자", result.getFirst().get(2));
        assertEquals("참여 인원", result.getFirst().get(3));
        assertEquals("비고", result.getFirst().get(4));
    }
}

