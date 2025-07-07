package org.spectre.kbank.dto.response

import java.math.BigDecimal

data class AccountBalanceResponseDto(
    val accountId: Long,
    val balance: BigDecimal
)