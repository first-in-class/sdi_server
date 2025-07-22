package com.realyoungk.sdi.controller;

import com.realyoungk.sdi.model.VisitModel;
import com.realyoungk.sdi.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/visits", method = {RequestMethod.GET, RequestMethod.POST})
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping(value = "")
    public String getVisits() {
        final LocalDateTime localDateTime = LocalDateTime.now();
        final Date startedAt = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final List<VisitModel> visitModels = visitService.fetchUpcoming(startedAt);

        StringBuilder sb = new StringBuilder();
        sb.append("[다가오는 탐방 일정]\n\n");

        for (VisitModel visitModel : visitModels) {
            long diff = (visitModel.getStartedAt().getTime() - startedAt.getTime()) / (1000 * 60 * 60 * 24);

            if (diff >= 0 && diff <= 2) {
                sb.append(visitModel.toDetailedString(diff));
                sb.append("\n");
            } else {
                sb.append(visitModel.toSimpleString(diff));
            }
        }

        return sb.toString();
    }

    @PostMapping("/new")
    public VisitModel postVisitsNew(@RequestBody VisitModel visitModel) {
        return visitService.save(visitModel);
    }
}
