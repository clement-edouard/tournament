package com.cedouard.service

import com.cedouard.dto.PlayerDto
import com.cedouard.exception.PlayerException
import com.cedouard.extension.toDto
import com.cedouard.model.Player
import com.cedouard.repository.PlayerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.litote.kmongo.Id
import java.io.File

private const val INIT_PLAYERS_FILE = "src/main/resources/data/players.json"

class PlayerService {
  private val playerRepository = PlayerRepository()
  private val objectMapper = ObjectMapper()

  init {
    playerRepository.deleteAll()
    val playersFile = File(INIT_PLAYERS_FILE)
    val players: List<Player> = objectMapper.readValue(playersFile)
    playerRepository.insertMany(players)
  }

  fun findAll(): List<Player> = playerRepository.findAll()

  fun findAllWithRank(): List<PlayerDto> {
    val sortedPlayers = findAll().map { it.toDto() }
    var rank = 1

    for (playerDto in sortedPlayers) {
      if (sortedPlayers.indexOf(playerDto) > 0
        && sortedPlayers[sortedPlayers.indexOf(playerDto) - 1].score != playerDto.score)
        rank++
      playerDto.rank = rank
    }
    return sortedPlayers
  }

  fun create(player: Player): Id<Player>? {
    playerRepository.findByPseudo(player.pseudo)?.let {
      throw PlayerException(ErrorResponse("Player ${player.pseudo} already exists"))
    } ?: playerRepository.insertOne(player)
    return player.id
  }

  fun update(id: String, score: Int): Boolean = playerRepository.updateOneById(id, score)

  fun findByPseudo(pseudo: String): Player? = playerRepository.findByPseudo(pseudo)

  fun deleteAll() = playerRepository.deleteAll()
}
