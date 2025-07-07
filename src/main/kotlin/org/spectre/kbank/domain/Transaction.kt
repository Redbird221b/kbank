package org.spectre.kbank.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.spectre.kbank.enums.TransactionTypes
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val transactionType: TransactionTypes,
    val amount: BigDecimal,
    val transactionDate: LocalDateTime,
    @ManyToOne
    val account: BankAccount
)