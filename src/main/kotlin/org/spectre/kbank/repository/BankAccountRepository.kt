package org.spectre.kbank.repository

import org.spectre.kbank.domain.BankAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface BankAccountRepository: JpaRepository<BankAccount, Long>