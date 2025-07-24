package com.realyoungk.sdi.controller;

import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.service.VisitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/visits", method = {RequestMethod.GET, RequestMethod.POST})
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping(value = "")
    public List<VisitModel> getVisits() {
        return visitService.fetchUpcoming();
    }

    @PostMapping("/new")
    public VisitModel postVisitsNew(@RequestBody VisitModel visitModel) {
        return visitService.save(visitModel);
    }
}
