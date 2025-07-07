package org.spectre.kbank.dto.response

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionResponseDto (
    val id: Long?,
    val accountId: Long?,
    val amount: BigDecimal,
    val transactionDate: LocalDateTime,
    val transactionType: String
)