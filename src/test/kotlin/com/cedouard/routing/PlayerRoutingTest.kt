package com.cedouard.routing

import com.cedouard.dto.PlayerDto
import com.cedouard.model.Player
import com.cedouard.plugin.configureSerialization
import com.cedouard.service.ErrorResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private const val CONTENT_TYPE = "Content-Type"
private const val CONTENT_TYPE_JSON = "application/json; charset=UTF-8"

class PlayerRoutingTest {

    @Test
    fun `Get all players respond 8 JSON players and OK Status`() = testApplication {
        application {
            configurePlayerRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val response = client.get("/player")
        val players = response.body<List<PlayerDto>>()
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(CONTENT_TYPE_JSON, response.headers[CONTENT_TYPE])
        assertEquals(8, players.size)
    }

    @Test
    fun `Get all players with rank respond 8 JSON players and OK Status`() = testApplication {
        application {
            configurePlayerRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val response = client.get("/player/rank")
        val players = response.body<List<PlayerDto>>()
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(CONTENT_TYPE_JSON, response.headers[CONTENT_TYPE])
        assertEquals(8, players.size)
    }

    @Test
    fun `Create new player respond USER_ID in header and CREATED Status`() = testApplication {
        application {
            configurePlayerRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val response = client.post("/player") {
            contentType(ContentType.Application.Json)
            setBody(PlayerDto(pseudo = "Joe", score = 42))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertNotNull(response.headers[USER_ID])
    }

    @Test
    fun `Duplicate pseudo respond CONFLICT Status`() = testApplication {
        application {
            configurePlayerRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val response = client.post("/player") {
            contentType(ContentType.Application.Json)
            setBody(PlayerDto(pseudo = "Marie", score = 42))
        }
        assertEquals(HttpStatusCode.Conflict, response.status)
        assertEquals(ErrorResponse("Player Marie already exists"), response.body())
    }

    @Test
    fun `Create player without pseudo respond BadRequest Status`() = testApplication {
        application {
            configurePlayerRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val response = client.post("/player") {
            contentType(ContentType.Application.Json)
            val playerWithoutPseudoJSON = "{\"score\": \"25\"}"
            setBody(playerWithoutPseudoJSON)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(ErrorResponse.MISSING_FIELDS, response.body())
    }

    @Test
    fun `Update player should respond OK status`() = testApplication {
        application {
            configurePlayerRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val playersResponse = client.get("/player")
        val players = playersResponse.body<List<PlayerDto>>()
        val marieId = players.find { "Marie" == it.pseudo }?.id
        val response = client.put("/player/$marieId") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(Player.SCORE, "25")
                    }
                )
            )
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `Update unknown player respond NotFound status`() = testApplication {
        application {
            configurePlayerRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val unknownId = "111111111111111111111111"
        val response = client.put("/player/$unknownId") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(Player.SCORE, "25")
                    }
                )
            )
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `Update player with invalid score`() = testApplication {
        application {
            configurePlayerRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val playersResponse = client.get("/player")
        val players = playersResponse.body<List<PlayerDto>>()
        val marieId = players.find { "Marie" == it.pseudo }?.id
        val response = client.put("/player/$marieId") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(Player.SCORE, "Hello world !")
                    }
                )
            )
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(ErrorResponse.SCORE_INTEGER, response.body())
    }

    @Test
    fun `Delete all players should return NO CONTENT status`() = testApplication {
        application {
            configurePlayerRouting()
        }

        val response = client.delete("/player")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }

}