package io.ticketo.ticket

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.ticketo.ticket.api.TicketsController
import io.ticketo.ticket.business.TicketAvailability
import io.ticketo.ticket.business.TicketService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@WebMvcTest(controllers = [TicketsController::class])
@MockkBean(TicketService::class)
@AutoConfigureRestDocs("build/generated-snippets/tickets-api")
class TicketsControllerTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val ticketService: TicketService
) {

    @Test
    fun `should get available tickets`() {
        val concertId = UUID.randomUUID()
        every { ticketService.availableTickets(concertId) } returns TicketAvailability(concertId, 50, 50)

        mockMvc.perform(get("/api/v1/tickets/concerts/{concertId}/availability", concertId))
            .andExpectAll(
                status().isOk
            ).andDo(
                document(
                    "ticket-availability", preprocessResponse(prettyPrint()), links(
                        halLinks(),
                        linkWithRel("purchase").description("link to the purchase operation"),
                        linkWithRel("self").description("link to self"),
                    ), responseFields(
                        subsectionWithPath("_links").ignored(),
                        fieldWithPath("concertId").description("The id of the concert"),
                        fieldWithPath("capacity").description("The people capacity of the concert"),
                        fieldWithPath("availableTickets").description("The current available tickets.")
                    )
                )
            )

    }

}