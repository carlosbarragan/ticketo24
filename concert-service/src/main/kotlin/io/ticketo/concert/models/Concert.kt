package io.ticketo.concert.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class Concert(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Concert>(ConcertTable)

    var concertName by ConcertTable.concertName
    var artistName by ConcertTable.artistName
    var description by ConcertTable.description
    var capacity by ConcertTable.capacity
}
