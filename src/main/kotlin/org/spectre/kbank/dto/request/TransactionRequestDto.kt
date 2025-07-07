package org.spectre.kbank.dto.request

import jakarta.validation.constraints.Positive
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

class TransactionRequestDto (
    @field:NotNull
    @field:Positive
    val amount: BigDecimal
)