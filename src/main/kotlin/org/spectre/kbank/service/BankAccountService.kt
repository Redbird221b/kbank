package org.spectre.kbank.service

import jakarta.transaction.Transactional
import org.spectre.kbank.domain.BankAccount
import org.spectre.kbank.domain.CurrentAccount
import org.spectre.kbank.domain.Customer
import org.spectre.kbank.domain.Transaction
import org.spectre.kbank.enums.TransactionTypes
import org.spectre.kbank.repository.BankAccountRepository
import org.spectre.kbank.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class BankAccountService @Autowired constructor(
    private val repository: BankAccountRepository,
    private val transactionService: TransactionService,
    private val customerService: CustomerService,
    private val transactionRepository: TransactionRepository
) {

    fun findBankAccountById(id: Long): BankAccount? {
        return repository.findById(id).orElse(null)
    }

    fun findBankAccountByCustomerId(customerId: Long): BankAccount? {
        val bankAccounts = repository.findAll()
        return bankAccounts.firstOrNull { it.accountHolder?.id == customerId }
    }

    fun createBankAccount(
        accountNumber: String,
        accountHolder: Customer,
        accountBalance: Double = 0.0,
        accountCreationDate: LocalDateTime,
        accountType: String,
    ): BankAccount {
        val newAccount = if (findBankAccountByCustomerId(accountHolder.id as Long) != null) {
            throw IllegalArgumentException("Customer already has an account")
        } else when (accountType) {
            "CURRENT" -> {
                CurrentAccount(accountNumber, accountBalance, accountHolder, accountCreationDate)
            }

            "SAVINGS" -> {
                CurrentAccount(accountNumber, accountBalance, accountHolder, accountCreationDate)
            }

            else -> throw IllegalArgumentException("Invalid account type: $accountType")
        }
        return repository.save(newAccount)
    }

    @Transactional
    fun makeDeposit(id: Long, amount: BigDecimal) {
        val account: BankAccount? = findBankAccountById(id)
        if (account == null) {
            throw IllegalArgumentException("Account does not exist")
        } else {
            if (amount < BigDecimal.ZERO) {
                throw IllegalArgumentException("Amount must be greater than 0")
            } else {
                account.accountBalance += amount
                repository.save(account)
                transactionService.createTransaction(
                    Transaction(
                        amount = amount,
                        transactionDate = LocalDateTime.now(),
                        account = account,
                        transactionType = TransactionTypes.DEPOSIT
                    )
                )
            }
        }

    }

    @Transactional
    fun makeWithdrawal(id: Long, amount: BigDecimal) {
        val account: BankAccount? = findBankAccountById(id)
        if (account == null) {
            throw IllegalArgumentException("Account does not exist")
        } else {
            if (amount < BigDecimal.ZERO) {
                throw IllegalArgumentException("Amount must be greater than 0")
            } else {
                if (account.accountBalance < amount) {
                    throw IllegalArgumentException("Insufficient funds")
                } else {
                    account.accountBalance -= amount
                    repository.save(account)
                    transactionService.createTransaction(
                        Transaction(
                            amount = amount,
                            transactionDate = LocalDateTime.now(),
                            account = account,
                            transactionType = TransactionTypes.WITHDRAWAL
                        )
                    )
                }
            }
        }
    }
    
    fun getAccountBalance(id: Long): BigDecimal {
        val account: BankAccount? = findBankAccountById(id)
        return account?.accountBalance ?: throw IllegalArgumentException("Account does not exist")
    }

}