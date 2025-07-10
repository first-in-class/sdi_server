package com.realyoungk.sdi.controller;

import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.service.VisitService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping
    public String visits() {
        return "visits";
    }

    @PostMapping("/new")
    public String postVisit(@RequestBody VisitModel visitModel) {
        visitService.saveVisit(visitModel);
        return "성공";
    }
}
