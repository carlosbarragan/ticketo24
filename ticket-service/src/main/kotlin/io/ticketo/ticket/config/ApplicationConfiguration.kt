package io.ticketo.ticket.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.IdGenerator
import org.springframework.util.JdkIdGenerator

@Configuration
class ApplicationConfiguration {

    @Bean
    fun idGenerator(): IdGenerator = JdkIdGenerator()
}