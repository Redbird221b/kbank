package org.spectre.kbank.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFound(
        ex: AccountNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        val error = ApiErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.NOT_FOUND.value(),
            error = "Account not found",
            message = ex.message ?: "Account not found",
            path = request.requestURI
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(CustomerNotFoundException::class)
    fun handleCustomerNotFound(
        ex: CustomerNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        val error = ApiErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.NOT_FOUND.value(),
            error = "Customer not found",
            message = ex.message ?: "Customer not found",
            path = request.requestURI
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidAccountTypeException::class)
    fun handleInvalidAccountType(
        ex: InvalidAccountTypeException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        val error = ApiErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Invalid account type",
            message = ex.message ?: "Invalid account type",
            path = request.requestURI
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InsufficientFundsException::class)
    fun handleInsufficientFunds(
        ex: InsufficientFundsException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        val error = ApiErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Insufficient funds",
            message = ex.message ?: "Insufficient funds",
            path = request.requestURI
        )
        return ResponseEntity(error,HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidTransactionAmountException::class)
    fun handleInvalidTransactionAmount(
        ex: InvalidTransactionAmountException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        val error = ApiErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Amount must be greater than 0",
            message = ex.message ?: "Amount must be greater than 0",
            path = request.requestURI
        )
        return ResponseEntity(error,HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DuplicateCustomerException::class)
    fun handleDuplicateCustomer(
        ex: DuplicateCustomerException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        val error = ApiErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Customer with this email is already registered",
            message = ex.message ?: "Customer with this email is already registered",
            path = request.requestURI
        )
        return ResponseEntity(error,HttpStatus.BAD_REQUEST)
    }
}