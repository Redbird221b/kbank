package org.spectre.kbank.dto.response

import java.math.BigDecimal
import java.time.LocalDateTime

data class BankAccountResponseDto(
    val id: Long?,
    val accountNumber: String,
    val accountHolderName: String,
    val balance: BigDecimal,
    val createdAt: LocalDateTime,
    val accountType: String // ← просто передавай type.name.lowercase()
)
