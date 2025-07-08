package org.spectre.kbank.service

import jakarta.transaction.Transactional
import org.spectre.kbank.domain.BankAccount
import org.spectre.kbank.domain.Customer
import org.spectre.kbank.domain.Transaction
import org.spectre.kbank.dto.request.CreateAccountRequestDto
import org.spectre.kbank.dto.request.TransactionRequestDto
import org.spectre.kbank.dto.response.AccountBalanceResponseDto
import org.spectre.kbank.dto.response.BankAccountResponseDto
import org.spectre.kbank.dto.response.TransactionResponseDto
import org.spectre.kbank.enums.AccountType
import org.spectre.kbank.enums.TransactionType
import org.spectre.kbank.exception.AccountNotFoundException
import org.spectre.kbank.exception.CustomerNotFoundException
import org.spectre.kbank.exception.DuplicateCustomerException
import org.spectre.kbank.exception.InsufficientFundsException
import org.spectre.kbank.exception.InvalidAccountTypeException
import org.spectre.kbank.exception.InvalidTransactionAmountException
import org.spectre.kbank.repository.BankAccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
class BankAccountService @Autowired constructor(
    private val repository: BankAccountRepository,
    private val transactionService: TransactionService,
    private val customerService: CustomerService
) {

    fun findBankAccountById(id: Long): BankAccount? {
        return repository.findById(id).orElse(null)
    }

    fun findBankAccountByCustomerId(customerId: Long): BankAccount? {
        val bankAccounts = repository.findAll()
        return bankAccounts.firstOrNull { it.accountHolder.id == customerId }
    }

    fun createAccount(request: CreateAccountRequestDto): BankAccount {
        val customer = customerService.getCustomerById(request.customerId)
            ?: throw CustomerNotFoundException("Customer with id ${request.customerId} not found")

        val accountNumber = generateAccountNumber()
        val now = LocalDateTime.now()

        return createBankAccount(
            accountNumber = accountNumber,
            accountHolder = customer,
            accountBalance = request.initialBalance,
            accountCreationDate = now,
            accountType = request.accountType
        )
    }

    fun createBankAccount(
        accountNumber: String,
        accountHolder: Customer,
        accountBalance: BigDecimal,
        accountCreationDate: LocalDateTime,
        accountType: String
    ): BankAccount {
        val type = try {
            AccountType.valueOf(accountType.uppercase())
        } catch (ex: IllegalArgumentException) {
            throw InvalidAccountTypeException("Invalid account type: $accountType.")
        }

        if (findBankAccountByCustomerId(accountHolder.id!!) != null) {
            throw DuplicateCustomerException("Customer already has an account")
        }

        val newAccount = BankAccount(
            accountNumber = accountNumber,
            accountHolder = accountHolder,
            accountBalance = accountBalance,
            accountCreationDate = accountCreationDate,
            accountType = type
        )
        return repository.save(newAccount)
    }

    private fun generateAccountNumber(): String {
        return "ACC" + UUID.randomUUID().toString().take(10).uppercase()
    }

    fun getAccountDtoById(id: Long): BankAccountResponseDto {
        val account = findBankAccountById(id)
            ?: throw AccountNotFoundException("Account with id $id not found")

        return BankAccountResponseDto(
            id = account.id,
            accountNumber = account.accountNumber,
            accountHolderName = account.accountHolder.name,
            balance = account.accountBalance,
            createdAt = account.accountCreationDate,
            accountType = account.accountType.name.lowercase()
        )
    }

    @Transactional
    fun deposit(id: Long, request: TransactionRequestDto) {
        makeDeposit(id, request.amount)
    }

    @Transactional
    fun withdraw(id: Long, request: TransactionRequestDto) {
        makeWithdrawal(id, request.amount)
    }

    fun getBalanceDto(id: Long): AccountBalanceResponseDto {
        return AccountBalanceResponseDto(
            accountId = id,
            balance = getAccountBalance(id)
        )
    }

    fun getTransactionsDto(accountId: Long): List<TransactionResponseDto> {
        return transactionService.getTransactionsForAccount(accountId).map {
            TransactionResponseDto(
                id = it.id,
                accountId = it.account.id,
                amount = it.amount,
                transactionDate = it.transactionDate,
                transactionType = it.transactionType.name.lowercase()
            )
        }
    }


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
                transactionType = TransactionType.DEPOSIT
            )
        )
    }


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
                transactionType = TransactionType.WITHDRAWAL
            )
        )
    }

    fun getAccountBalance(id: Long): BigDecimal {
        val account: BankAccount? = findBankAccountById(id)
        return account?.accountBalance ?: throw AccountNotFoundException("Account does not exist")
    }

}