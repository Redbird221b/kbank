package org.spectre.kbank.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type")
abstract class BankAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null,

    @Column(nullable = false, unique = true)
    val accountNumber: String = "",

    @ManyToOne
    @JoinColumn(name = "customer_id")
    val accountHolder: Customer? = null,

    @Column(nullable = false, unique = true)
    var accountBalance: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, unique = true)
    val accountCreationDate: LocalDateTime = LocalDateTime.now()
)
