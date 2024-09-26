package io.ticketo.ticket

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID


@Table("purchased_tickets")
class PurchasedTicket(
    @Id val id: UUID? = null,
    @Column("concert_id") val concertId: UUID,
    val purchasedAt: Instant? = null,
    val reservedAt: Instant? = null
)