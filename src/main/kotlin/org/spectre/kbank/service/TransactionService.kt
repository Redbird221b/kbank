package org.spectre.kbank.service

import jakarta.transaction.Transactional
import org.spectre.kbank.domain.Transaction
import org.spectre.kbank.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TransactionService @Autowired constructor(
    private val repository: TransactionRepository
) {
    fun getTransactionsForAccount(accountId: Long) = repository.findByAccountId(
        accountId
    )

    @Transactional
    fun createTransaction(transaction: Transaction): Transaction = repository.save(transaction)
}