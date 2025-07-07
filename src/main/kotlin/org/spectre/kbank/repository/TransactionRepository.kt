package org.spectre.kbank.repository

import org.spectre.kbank.domain.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository: JpaRepository<Transaction, Long> {
    fun findByAccountId(accountId: Long): List<Transaction>
}
