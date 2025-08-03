package com.realyoungk.sdi.controller

import com.realyoungk.sdi.model.VisitModel
import com.realyoungk.sdi.service.VisitService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import java.util.*

class VisitControllerTest {

    @Mock
    private lateinit var visitService: VisitService
    
    private lateinit var visitController: VisitController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        visitController = VisitController(visitService)
    }

    @Test
    fun `getVisits가 VisitService의 fetchUpcoming을 호출한다`() {
        // given
        val expectedVisits = listOf(
            VisitModel(
                id = "1",
                companyName = "테스트 회사",
                startedAt = Date(),
                finishedAt = null,
                participantCount = "10명",
                teamName = "개발팀",
                organizer = "홍길동",
                remark = "테스트"
            )
        )
        `when`(visitService.fetchUpcoming()).thenReturn(expectedVisits)

        // when
        val result = visitController.getVisits()

        // then
        verify(visitService).fetchUpcoming()
        assertThat(result).isEqualTo(expectedVisits)
    }

    @Test
    fun `postVisitsNew가 VisitService의 save를 호출한다`() {
        // given
        val visitModel = VisitModel(
            id = "1",
            companyName = "테스트 회사",
            startedAt = Date(),
            finishedAt = null,
            participantCount = "10명",
            teamName = "개발팀",
            organizer = "홍길동",
            remark = "테스트"
        )
        `when`(visitService.save(visitModel)).thenReturn(visitModel)

        // when
        val result = visitController.postVisitsNew(visitModel)

        // then
        verify(visitService).save(visitModel)
        assertThat(result).isEqualTo(visitModel)
    }
}