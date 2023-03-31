package com.cedouard.model

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class Player(
    @BsonId
    val id: Id<Player>? = null, val pseudo: String = "", val score: Int = 0
) {
    companion object {
        const val ID = "id"
        const val PSEUDO = "pseudo"
        const val SCORE = "score"
    }
}