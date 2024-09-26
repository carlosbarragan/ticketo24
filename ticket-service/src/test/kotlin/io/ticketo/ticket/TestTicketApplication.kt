package io.ticketo.ticket

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<TicketApplication>().with(TestcontainersConfiguration::class).run(*args)
}
