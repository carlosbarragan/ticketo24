package io.ticketo.ticket.external

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.util.UUID

@Component
class ConcertClient(@Value("\${services.concerts.url}") private val concertsUrl: String) {

    private val restClient = RestClient.create(concertsUrl)

    fun fetchConcertById(id: UUID): Concert {
        return restClient.get().uri("/concerts/{id}", id).retrieve().body(Concert::class.java)
            ?: throw ConcertNotFoundException("Concert not found for id $id")
    }


}

data class Concert(val id: UUID, val capacity: Int)

class ConcertNotFoundException(msg: String) : RuntimeException(msg)