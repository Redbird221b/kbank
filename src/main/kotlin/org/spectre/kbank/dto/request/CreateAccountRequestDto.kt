package org.spectre.kbank.dto.request

import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class CreateAccountRequestDto(
    val customerId: Long,
    val accountType: String,
    @field:Positive
    val initialBalance: BigDecimal
)
