package org.spectre.kbank.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.spectre.kbank.enums.AccountType
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
data class BankAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val accountNumber: String,

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    val accountHolder: Customer,

    @Column(name = "account_balance", nullable = false)
    var accountBalance: BigDecimal = BigDecimal.ZERO,

    @Column(name = "account_creation_date", nullable = false)
    val accountCreationDate: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    val accountType: AccountType
)
