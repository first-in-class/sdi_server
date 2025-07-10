package com.realyoungk.sdi.controller;

import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.service.VisitService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/visits")
public class VisitController {

    private VisitService visitService;

    @GetMapping
    public String visits() {
        return "visits";
    }

    @PostMapping("/new")
    public String postVisit(@RequestBody VisitModel visitModel) {
        // TODO: 데이터베이스에 저장
        return "성공";
    }
}
