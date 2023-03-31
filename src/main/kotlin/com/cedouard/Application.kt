package com.cedouard

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.cedouard.plugin.*
import com.cedouard.routing.configurePlayerRouting

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSwagger()
    configureSerialization()
    configurePlayerRouting()
}
