package org.spectre.kbank.service

import org.spectre.kbank.domain.Transaction
import org.spectre.kbank.repository.BankAccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BankAccountService @Autowired constructor(
    private val repository: BankAccountRepository
) {
    fun findByBankAccountId(accountId: Long): List<Transaction> {

    }
}