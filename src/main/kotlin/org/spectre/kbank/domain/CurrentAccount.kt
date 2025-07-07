package org.spectre.kbank.domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
@DiscriminatorValue("CURRENT")
class CurrentAccount(
    accountNumber: String,
    accountBalance: Double,
    accountHolder: Customer,
    accountCreationDate: LocalDateTime,
) : BankAccount()