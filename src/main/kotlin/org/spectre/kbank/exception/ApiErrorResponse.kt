package org.spectre.kbank.exception

import java.time.LocalDateTime

class ApiErrorResponse(
    timestamp: LocalDateTime,
    status: Int,
    error: String,
    message: String,
    path: String
) {
}