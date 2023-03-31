package com.cedouard.repository

import com.cedouard.model.Player
import com.mongodb.client.result.InsertOneResult
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.id.toId

class PlayerRepository {
    private val client = KMongo.createClient()
    private val database = client.getDatabase(PLAYERS_DB_NAME)
    private val playerCollection = database.getCollection<Player>()

    fun findAll(): List<Player> =
        playerCollection.find()
            .descendingSort(Player::score)
            .toList()

    fun findByPseudo(pseudo: String): Player? = playerCollection.findOne(Player::pseudo eq pseudo)

    fun insertOne(player: Player): InsertOneResult = playerCollection.insertOne(player)

    fun insertMany(players: List<Player>): Any = playerCollection.insertMany(players)

    fun updateOneById(id: String, score: Int): Boolean {
        val playerId = ObjectId(id).toId<Player>()
        return playerCollection.updateOneById(playerId, setValue(Player::score, score)).modifiedCount > 0
    }

    fun deleteAll() = playerCollection.deleteMany(ALL_DOCUMENTS_FILTER)

    companion object {
        private const val PLAYERS_DB_NAME = "players"
        private const val ALL_DOCUMENTS_FILTER = "{}"
    }
}
