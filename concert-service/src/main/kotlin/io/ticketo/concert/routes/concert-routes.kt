package io.ticketo.concert.routes

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ticketo.concert.models.Concert
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

@Serializable
data class ConcertDTO(
    @Serializable(UUIDSerializer::class) val id: UUID,
    val concertName: String,
    val artistName: String,
    val description: String,
    val capacity: Int

)

@Serializable
class ConcertsDTO(val concerts: List<ConcertDTO>)

fun Route.concertRoutes() {
    route("/api/v1/concerts") {
        get {
            val concerts = transaction {
                Concert.all().map {
                    ConcertDTO(it.id.value, it.concertName, it.artistName, it.description, it.capacity)
                }
            }
            call.respond(ConcertsDTO(concerts))
        }
        get("/{id}") {
            val concertId: UUID =
                call.parameters["id"]?.toUUIDOrThrow() ?: error("Missing or malformed id")


            val concert = transaction {
                Concert.findById(concertId)?.let {
                    ConcertDTO(
                        it.id.value,
                        it.concertName,
                        it.artistName,
                        it.description,
                        it.capacity
                    )
                } ?: throw ConcertNotFoundException("Concert for id $concertId not found")
            }

            call.respond(concert)
        }
    }


}

class InvalidParameterException(msg: String) : RuntimeException(msg)
class ConcertNotFoundException(msg: String) : RuntimeException(msg)

fun String.toUUIDOrThrow(): UUID = try {
    UUID.fromString(this)
} catch (e: IllegalArgumentException) {
    throw InvalidParameterException("Invalid paramter $this")
}


object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}