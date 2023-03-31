package com.cedouard.extension

import com.cedouard.dto.PlayerDto
import com.cedouard.model.Player

fun Player.toDto(): PlayerDto =
  PlayerDto(
    id = this.id.toString(),
    pseudo = this.pseudo,
    score = this.score
  )

fun PlayerDto.toPlayer(): Player =
  Player(
    pseudo = this.pseudo,
    score = this.score
  )