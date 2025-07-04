package org.spectre.kbank.domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("CURRENT")
class CurrentAccount(): BankAccount()