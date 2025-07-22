package com.realyoungk.sdi.controller;

import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/visits")
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("")
    public List<VisitModel> getVisits(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startedAt) {
        return visitService.fetchUpcoming(startedAt);
    }

    @PostMapping("/new")
    public VisitModel postVisitsNew(@RequestBody VisitModel visitModel) {
        return visitService.save(visitModel);
    }
}
