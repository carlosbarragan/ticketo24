package io.ticketo.ticket.business

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FeatureService(
    @Value("\${features.reservation.enabled}") val isReservationEnabled: Boolean,
    @Value("\${features.reservation.minimumTicketAvailability}") val minimumTicketAvailability: Long,
)