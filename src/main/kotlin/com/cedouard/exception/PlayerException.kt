package com.cedouard.exception

import com.cedouard.service.ErrorResponse

class PlayerException(val errorResponse: ErrorResponse) : Exception()