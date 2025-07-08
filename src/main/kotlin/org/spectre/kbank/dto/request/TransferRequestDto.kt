package org.spectre.kbank.dto.request

import java.math.BigDecimal

class TransferRequestDto (
    val fromAccountId: Long,
    val toAccountId: Long,
    val amount: BigDecimal,
)