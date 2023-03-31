package com.cedouard.service

import com.cedouard.exception.PlayerException
import com.cedouard.model.Player
import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PlayerServiceTest {

    private val playerService = PlayerService()

    @Test
    fun `Init players ranking is good`() {
        val players = playerService.findAllWithRank()
        assertEquals(1, players.first().rank)
        assertEquals(7, players.last().rank)
        assertEquals(3, players.find { "Marie" == it.pseudo }?.rank)
    }

    @Test
    fun `Players ranking after adding Joe is good`() {
        playerService.create(Player(pseudo = "Joe", score = 42))
        val players = playerService.findAllWithRank()
        assertEquals(9, players.size)
        assertEquals(4, players.find { "Joe" == it.pseudo }?.rank)
    }

    @Test
    fun `No players after delete all`() {
        playerService.deleteAll()
        val players = playerService.findAll()
        assertEquals(0, players.size)
    }

    @Test
    fun `Marie becomes the best player after update`() {
        val player = playerService.findByPseudo("Marie")
        playerService.update(player?.id.toString(), 1337)
        val players = playerService.findAllWithRank()
        assertEquals(1, players.find { "Marie" == it.pseudo }?.rank)
    }

    @Test
    fun `New player with existing pseudo should throw exception`() {
        val exception = assertThrows(PlayerException::class.java) {
            playerService.create(Player(pseudo = "Marie", score = 51))
        }
        assertEquals(ErrorResponse("Player Marie already exists"), exception.errorResponse)
    }

    @Test
    fun `Search unknown pseudo should return nothing`() {
        val player = playerService.findByPseudo("Julien")
        assertNull(player)
    }

    @Test
    fun `Two players with the same score should have the same ranking`() {
        playerService.create(Player(pseudo = "Vincent", score = 32))
        playerService.create(Player(pseudo = "Salah", score = 32))
        val players = playerService.findAllWithRank()
        assertEquals(players.find { "Vincent" == it.pseudo }?.rank, players.find { "Salah" == it.pseudo }?.rank)
    }

}