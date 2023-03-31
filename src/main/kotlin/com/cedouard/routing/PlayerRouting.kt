package com.cedouard.routing

import com.cedouard.dto.PlayerDto
import com.cedouard.exception.PlayerException
import com.cedouard.extension.toDto
import com.cedouard.extension.toPlayer
import com.cedouard.model.Player
import com.cedouard.service.ErrorResponse
import com.cedouard.service.PlayerService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val USER_ID = "player-id"

fun Application.configurePlayerRouting() {
    val service = PlayerService()

    routing {
        route("player") {
            get("/rank") {
                val users =
                    service.findAllWithRank()
                call.respond(users)
            }
            get {
                val users =
                    service.findAll()
                        .map(Player::toDto)
                call.respond(users)
            }
            post {
                try {
                    val request = call.receive<PlayerDto>()
                    val player = request.toPlayer()
                    service.create(player)
                        ?.let { userId ->
                            call.response.headers.append(USER_ID, userId.toString())
                            call.respond(HttpStatusCode.Created)
                        } ?: call.respond(HttpStatusCode.InternalServerError, ErrorResponse.USER_CREATION_ERROR)
                } catch (mfe: BadRequestException) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse.MISSING_FIELDS)
                } catch (pe: PlayerException) {
                    call.respond(HttpStatusCode.Conflict, pe.errorResponse)
                }
            }
            put("/{id}") {
                val id = call.parameters[Player.ID].toString()
                val score = call.receiveParameters()[Player.SCORE]?.toIntOrNull()
                score?.let {
                    when(service.update(id, score)) {
                        true -> call.respond(HttpStatusCode.OK)
                        false -> call.respond(HttpStatusCode.NotFound)
                    }
                } ?: call.respond(HttpStatusCode.BadRequest, ErrorResponse.SCORE_INTEGER)
            }
            delete {
                service.deleteAll()
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
