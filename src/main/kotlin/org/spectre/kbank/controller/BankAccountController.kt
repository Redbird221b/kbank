package org.spectre.kbank.controller

import org.spectre.kbank.dto.request.CreateAccountRequestDto
import org.spectre.kbank.dto.request.TransactionRequestDto
import org.spectre.kbank.dto.response.AccountBalanceResponseDto
import org.spectre.kbank.dto.response.BankAccountResponseDto
import org.spectre.kbank.dto.response.TransactionResponseDto
import org.spectre.kbank.exception.AccountNotFoundException
import org.spectre.kbank.exception.CustomerNotFoundException
import org.spectre.kbank.service.BankAccountService
import org.spectre.kbank.service.CustomerService
import org.spectre.kbank.service.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/accounts")
class BankAccountController @Autowired constructor(
    private val bankAccountService: BankAccountService,
    private val customerService: CustomerService,
    private val transactionService: TransactionService
) {
    @PostMapping
    fun createAccount(
        @RequestBody
        request: CreateAccountRequestDto
    ): ResponseEntity<String> {
        val customer = customerService.getCustomerById(request.customerId)
            ?: throw CustomerNotFoundException("Customer with id ${request.customerId} not found")
        val account = bankAccountService.createBankAccount(
            accountNumber = generateAccountNumber(),
            accountHolder = customer,
            accountType = request.accountType.uppercase(),
            accountCreationDate = LocalDateTime.now(),
            accountBalance = request.initialBalance
        )

        return ResponseEntity.status(HttpStatus.CREATED).body("Account ${account.accountNumber} created")
    }

    @GetMapping("/{id}")
    fun getAccountById(@PathVariable id: Long): ResponseEntity<Any> {
        val account = bankAccountService.findBankAccountById(id)
            ?: throw AccountNotFoundException("Account with id $id not found")

        val dto = BankAccountResponseDto(
            id = account.id,
            accountNumber = account.accountNumber,
            accountHolderName = account.accountHolder?.name ?: "Unknown",
            balance = account.accountBalance,
            createdAt = account.accountCreationDate,
            accountType = account.javaClass.simpleName.lowercase()
        )

        return ResponseEntity.ok(dto)
    }

    @PostMapping("/{id}/deposit")
    fun deposit(
        @PathVariable id: Long,
        @RequestBody request: TransactionRequestDto,
    ): ResponseEntity<String> {
        bankAccountService.makeDeposit(id, request.amount)
        return ResponseEntity.ok("Deposit of ${request.amount} successful to account ID $id")
    }

    @PostMapping("/{id}/withdraw")
    fun withdraw(
        @PathVariable id: Long,
        @RequestBody request: TransactionRequestDto,
    ): ResponseEntity<String> {
        bankAccountService.makeWithdrawal(id, request.amount)
        return ResponseEntity.ok("Withdrawal of ${request.amount} successful from account ID $id")
    }

    @GetMapping("/{id}/balance")
    fun getBalance(
        @PathVariable id: Long
    ): ResponseEntity<AccountBalanceResponseDto> {
        val balance = bankAccountService.getAccountBalance(id)
        return ResponseEntity.ok(AccountBalanceResponseDto(id, balance))
    }

    @GetMapping("/{id}/transactions")
    fun getTransactions(
        @PathVariable id: Long
    ): ResponseEntity<List<TransactionResponseDto>> {
        val transactions = transactionService.getTransactionsForAccount(id)
        val dtoList = transactions.map {
            TransactionResponseDto(
                id = it.id,
                accountId = it.account.id,
                amount = it.amount,
                transactionDate = it.transactionDate,
                transactionType = it.transactionType.name.lowercase()
            )
        }
        return ResponseEntity.ok(dtoList)
    }


    fun generateAccountNumber(): String {
        return "ACC" + UUID.randomUUID().toString().take(10).uppercase()
    }

}



