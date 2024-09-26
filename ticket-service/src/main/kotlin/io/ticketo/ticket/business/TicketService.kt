package io.ticketo.ticket.business

import io.ticketo.ticket.PurchasedTicket
import io.ticketo.ticket.external.ConcertClient
import io.ticketo.ticket.persistence.PurchasedTicketsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class TicketService(
    private val purchasedTicketsRepository: PurchasedTicketsRepository,
    private val concertClient: ConcertClient
) {

    fun availableTickets(concertId: UUID): TicketAvailability {
        val concert = concertClient.fetchConcertById(concertId)
        val purchasedTickets = purchasedTicketsRepository.countByConcertId(concertId)
        val availableTickets = concert.capacity - purchasedTickets
        return TicketAvailability(concertId, concert.capacity, availableTickets)
    }

    @Transactional
    fun purchaseTicketFor(concertId: UUID): PurchasedTicket {
        val ticketAvailability = availableTickets(concertId)
        if (ticketAvailability.availableTickets <= 0) {
            throw NoMoreTicketsAvailableException("No more tickets available for concert: $concertId")
        }

        val purchasedTicket = PurchasedTicket(concertId = concertId)
        return purchasedTicketsRepository.save(purchasedTicket)
    }

    fun getPurchasedTicketById(ticketId: UUID): PurchasedTicket = purchasedTicketsRepository.findByIdOrNull(ticketId)
        ?: throw TicketNotFoundException("No ticket found for $ticketId")
}


class TicketAvailability(val concertId: UUID, val capacity: Int, val availableTickets: Long)
class NoMoreTicketsAvailableException(msg: String) : RuntimeException(msg)
class TicketNotFoundException(msg: String) : RuntimeException(msg)