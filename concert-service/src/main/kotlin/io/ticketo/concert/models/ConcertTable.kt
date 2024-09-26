package io.ticketo.concert.models

import org.jetbrains.exposed.dao.id.UUIDTable

object ConcertTable : UUIDTable("concerts") {
    val concertName = varchar("concert_name", 255)
    val artistName = varchar("artist_name", 255)
    val description = text("description")
    val capacity = integer("capacity")
}