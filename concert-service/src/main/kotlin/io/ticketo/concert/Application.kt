package io.ticketo.concert

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ticketo.concert.routes.ConcertNotFoundException
import io.ticketo.concert.routes.InvalidParameterException
import io.ticketo.concert.routes.concertRoutes
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception<Throwable> { call, cause -> exceptionHandler(call, cause) }
    }
    install(CORS) {
        anyHost()
    }

    val dbUrl = environment.config.property("database.url").getString()
    val dbUser = environment.config.property("database.user").getString()
    val dbPassword = environment.config.property("database.password").getString()


    val flyway = Flyway.configure()
        .dataSource(dbUrl, dbUser, dbPassword)
        .load()

    flyway.migrate()

    Database.connect(
        url = dbUrl,
        driver = environment.config.property("database.driver").getString(),
        user = dbUser,
        password = dbPassword
    )

    routing {
        concertRoutes()
    }

}

suspend fun exceptionHandler(call: ApplicationCall, cause: Throwable) {
    when (cause) {
        is InvalidParameterException -> {
            call.respond(HttpStatusCode.BadRequest)
        }

        is ConcertNotFoundException -> {
            call.respond(HttpStatusCode.NotFound)
        }

        else -> {
            call.respond(HttpStatusCode.InternalServerError)
        }

    }
}