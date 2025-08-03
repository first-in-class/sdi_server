package com.realyoungk.sdi.controller

import com.realyoungk.sdi.model.VisitModel
import com.realyoungk.sdi.service.VisitService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/visits")
class VisitController(private val visitService: VisitService) {

    @GetMapping
    fun getVisits(): List<VisitModel> {
        return visitService.fetchUpcoming()
    }

    @PostMapping("/new")
    fun postVisitsNew(@RequestBody visitModel: VisitModel): VisitModel {
        return visitService.save(visitModel)
    }
}