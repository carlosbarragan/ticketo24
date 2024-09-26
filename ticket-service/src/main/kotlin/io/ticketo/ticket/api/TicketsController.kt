package io.ticketo.ticket.api

import io.ticketo.ticket.PurchasedTicket
import io.ticketo.ticket.business.TicketAvailability
import io.ticketo.ticket.business.TicketService
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/v1/tickets", produces = ["application/hal+json"])
class TicketsController(
    private val ticketService: TicketService
) {

    @GetMapping("/concerts/{concertId}/availability")
    fun fetchAvailableTicketsForConcert(@PathVariable concertId: UUID): TicketAvailabilityDto {
        val ticketAvailability = ticketService.availableTickets(concertId)
        val ticketAvailabilityDto = ticketAvailability.toDtoWithSelfLink()
        if (ticketAvailability.isPurchasable) {
            ticketAvailabilityDto += linkTo<TicketsController> { purchaseTicket(concertId) }.withRel("purchase")
        }
        return ticketAvailabilityDto
    }

    @PostMapping("/concerts/{concertId}/ticket")
    fun purchaseTicket(@PathVariable concertId: UUID): ResponseEntity<PurchasedTicketDto> {
        val purchasedTicket = ticketService.purchaseTicketFor(concertId)
        val purchasedTicketDto = purchasedTicket.toDtoWithSelfLink()
        val locationUri = selfRefLinkForTicket(purchasedTicketDto.ticketId).toUri()

        return ResponseEntity.created(locationUri).body(purchasedTicketDto)
    }

    @GetMapping("/{ticketId}")
    fun fetchTicket(@PathVariable ticketId: UUID): PurchasedTicketDto {
        return ticketService.getPurchasedTicketById(ticketId).toDtoWithSelfLink()
    }

}

fun TicketAvailability.toDtoWithSelfLink(): TicketAvailabilityDto =
    TicketAvailabilityDto(concertId, capacity, availableTickets).also {
        it += linkTo<TicketsController> { fetchAvailableTicketsForConcert(concertId) }.withSelfRel()
    }

fun PurchasedTicket.toDtoWithSelfLink(): PurchasedTicketDto = PurchasedTicketDto(id!!, concertId, purchasedAt).apply {
    add(selfRefLinkForTicket(ticketId))
}

fun selfRefLinkForTicket(ticketId: UUID): Link =
    linkTo<TicketsController> { fetchTicket(ticketId) }.withRel("self")

operator fun <T : RepresentationModel<T>> RepresentationModel<T>.plusAssign(link: Link) {
    add(link)
}

open class PurchasedTicketDto(val ticketId: UUID, val concertId: UUID, val purchasedAt: Instant) :
    RepresentationModel<PurchasedTicketDto>()

open class TicketAvailabilityDto(
    val concertId: UUID,
    val capacity: Int,
    val availableTickets: Long,
) : RepresentationModel<TicketAvailabilityDto>()