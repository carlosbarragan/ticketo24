package io.ticketo.ticket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType

@SpringBootApplication
@EnableHypermediaSupport(type = [HypermediaType.HAL])
class TicketApplication

fun main(args: Array<String>) {
    runApplication<TicketApplication>(*args)
}


