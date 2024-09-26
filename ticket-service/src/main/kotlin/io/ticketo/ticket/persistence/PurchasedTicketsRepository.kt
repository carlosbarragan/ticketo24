package io.ticketo.ticket.persistence

import io.ticketo.ticket.PurchasedTicket
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface PurchasedTicketsRepository : CrudRepository<PurchasedTicket, UUID> {
    fun countByConcertId(concertId: UUID): Long
}