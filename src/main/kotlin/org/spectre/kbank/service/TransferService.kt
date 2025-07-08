package org.spectre.kbank.service

import jakarta.transaction.Transactional
import org.spectre.kbank.domain.Transaction
import org.spectre.kbank.domain.Transfer
import org.spectre.kbank.dto.request.TransferRequestDto
import org.spectre.kbank.enums.TransactionType
import org.spectre.kbank.exception.AccountNotFoundException
import org.spectre.kbank.exception.InsufficientFundsException
import org.spectre.kbank.exception.InvalidTransactionAmountException
import org.spectre.kbank.exception.SameAccountTransferException
import org.spectre.kbank.repository.BankAccountRepository
import org.spectre.kbank.repository.TransferRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.minus
import kotlin.plus

@Service
class TransferService @Autowired constructor(
    private val transferRepository: TransferRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val transactionService: TransactionService,
    private val bankAccountService: BankAccountService
) {
    @Transactional
    fun transferFunds(request: TransferRequestDto): Transfer {
        val fromAccount = bankAccountService.findBankAccountById(request.fromAccountId)
            ?: throw AccountNotFoundException("Account not found")
        val toAccount = bankAccountService.findBankAccountById(request.toAccountId)
            ?: throw AccountNotFoundException("Account not found")

        if (request.amount <= BigDecimal.ZERO) throw InvalidTransactionAmountException("Amount must be greater than 0")
        if (request.amount > fromAccount.accountBalance) throw InsufficientFundsException("Insufficient funds")
        if (request.fromAccountId == request.toAccountId)
            throw SameAccountTransferException("Cannot transfer to the same account")

        fromAccount.accountBalance -= request.amount
        toAccount.accountBalance += request.amount

        val transactionDate = LocalDateTime.now()
        val transactionFrom = transactionService.createTransaction(
            Transaction(
                amount = request.amount,
                transactionDate = transactionDate,
                account = fromAccount,
                transactionType = TransactionType.WITHDRAWAL
            )
        )

        val transactionTo = transactionService.createTransaction(
            Transaction(
                amount = request.amount,
                transactionDate = transactionDate,
                account = toAccount,
                transactionType = TransactionType.DEPOSIT
            )
        )

        bankAccountRepository.save(fromAccount)
        bankAccountRepository.save(toAccount)

        return transferRepository.save(
            Transfer(
                fromAccount = fromAccount,
                toAccount = toAccount,
                amount = request.amount,
                createdAt = transactionDate,
                transactionIdFrom = transactionFrom.id,
                transactionIdTo = transactionTo.id
            )
        )
    }


}