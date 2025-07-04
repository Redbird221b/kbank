package org.spectre.kbank.domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.math.BigDecimal

@Entity
@DiscriminatorValue("SAVINGS")
class SavingsAccount(): BankAccount()