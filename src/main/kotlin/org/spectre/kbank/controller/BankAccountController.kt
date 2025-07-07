package org.spectre.kbank.controller

import org.spectre.kbank.dto.request.CreateAccountRequestDto
import org.spectre.kbank.dto.request.TransactionRequestDto
import org.spectre.kbank.service.BankAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class BankAccountController @Autowired constructor(
    private val bankAccountService: BankAccountService,
) {
    @PostMapping
    fun createAccount(@RequestBody request: CreateAccountRequestDto): ResponseEntity<String> {
        val account = bankAccountService.createAccount(request)
        return ResponseEntity.status(HttpStatus.CREATED).body("Account ${account.accountNumber} created")
    }

    @GetMapping("/{id}")
    fun getAccountById(@PathVariable id: Long) =
        ResponseEntity.ok(bankAccountService.getAccountDtoById(id))

    @PostMapping("/{id}/deposit")
    fun deposit(@PathVariable id: Long, @RequestBody request: TransactionRequestDto) =
        ResponseEntity.ok("Deposit successful").also {
            bankAccountService.deposit(id, request)
        }

    @PostMapping("/{id}/withdraw")
    fun withdraw(@PathVariable id: Long, @RequestBody request: TransactionRequestDto) =
        ResponseEntity.ok("Withdraw successful").also {
            bankAccountService.withdraw(id, request)
        }

    @GetMapping("/{id}/balance")
    fun getBalance(@PathVariable id: Long) =
        ResponseEntity.ok(bankAccountService.getBalanceDto(id))

    @GetMapping("/{id}/transactions")
    fun getTransactions(@PathVariable id: Long) =
        ResponseEntity.ok(bankAccountService.getTransactionsDto(id))
}



