package com.cedouard.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(
    val id: String? = null,
    val pseudo: String,
    var score: Int,
    var rank: Int? = null
)