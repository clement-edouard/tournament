package com.cedouard.plugin

import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureSwagger() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "src/main/resources/openapi/documentation.yaml")
    }
}
