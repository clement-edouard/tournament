package com.cedouard.service

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val message: String) {
  companion object {
    val SCORE_INTEGER = ErrorResponse("Score should be an integer")
    val USER_CREATION_ERROR = ErrorResponse("Player creation fail")
    val MISSING_FIELDS = ErrorResponse("Field(s) pseudo and/or score are missing")
  }
}