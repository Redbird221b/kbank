package org.spectre.kbank.service

import jakarta.transaction.Transactional
import org.spectre.kbank.domain.BankAccount
import org.spectre.kbank.domain.Customer
import org.spectre.kbank.domain.Transaction
import org.spectre.kbank.enums.AccountTypes
import org.spectre.kbank.enums.TransactionTypes
import org.spectre.kbank.exception.AccountNotFoundException
import org.spectre.kbank.exception.DuplicateCustomerException
import org.spectre.kbank.exception.InsufficientFundsException
import org.spectre.kbank.exception.InvalidAccountTypeException
import org.spectre.kbank.exception.InvalidTransactionAmountException
import org.spectre.kbank.repository.BankAccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class BankAccountService @Autowired constructor(
    private val repository: BankAccountRepository,
    private val transactionService: TransactionService,
) {

    fun findBankAccountById(id: Long): BankAccount? {
        return repository.findById(id).orElse(null)
    }

    fun findBankAccountByCustomerId(customerId: Long): BankAccount? {
        val bankAccounts = repository.findAll()
        return bankAccounts.firstOrNull { it.accountHolder.id == customerId }
    }

    fun createBankAccount(
        accountNumber: String,
        accountHolder: Customer,
        accountBalance: BigDecimal,
        accountCreationDate: LocalDateTime,
        accountType: String
    ): BankAccount {
        val type = try {
            AccountTypes.valueOf(accountType)
        } catch (ex: InvalidAccountTypeException) {
            throw InvalidAccountTypeException("Invalid account type: $accountType. ")
        }

        if (findBankAccountByCustomerId(accountHolder.id!!) != null) {
            throw DuplicateCustomerException("Customer already has an account")
        }

        val newAccount = BankAccount(
            accountNumber = accountNumber,
            accountBalance = accountBalance,
            accountHolder = accountHolder,
            accountCreationDate = accountCreationDate,
            accountType = type
        )
        return repository.save(newAccount)
    }


    @Transactional
    fun makeDeposit(id: Long, amount: BigDecimal) {
        val account = findBankAccountById(id) ?: throw AccountNotFoundException("Account not found")
        if (amount <= BigDecimal.ZERO) throw InvalidTransactionAmountException("Amount must be greater than 0")

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


    @Transactional
    fun makeWithdrawal(id: Long, amount: BigDecimal) {
        val account = findBankAccountById(id) ?: throw AccountNotFoundException("Account not found")
        if (amount <= BigDecimal.ZERO) throw InvalidTransactionAmountException("Amount must be greater than 0")
        if (account.accountBalance < amount) throw InsufficientFundsException("Insufficient funds")
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

    fun getAccountBalance(id: Long): BigDecimal {
        val account: BankAccount? = findBankAccountById(id)
        return account?.accountBalance ?: throw AccountNotFoundException("Account does not exist")
    }

}