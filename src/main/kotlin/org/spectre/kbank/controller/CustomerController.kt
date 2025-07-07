package org.spectre.kbank.controller

import org.spectre.kbank.dto.request.CustomerRequestDto
import org.spectre.kbank.dto.response.CustomerResponseDto
import org.spectre.kbank.exception.CustomerNotFoundException
import org.spectre.kbank.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.spectre.kbank.mapper.toResponseDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomerController @Autowired constructor(
    private val customerService: CustomerService
) {
    @PostMapping
    fun create(@RequestBody request: CustomerRequestDto): ResponseEntity<CustomerResponseDto> {
        val customer = customerService.createCustomer(request.name, request.phoneNumber, request.email)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(customer.toResponseDto())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<CustomerResponseDto> {
        val customer = customerService.getCustomerById(id)
            ?: throw CustomerNotFoundException("Customer with ID $id not found")
        return ResponseEntity.ok(customer.toResponseDto())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<String> {
        val customer = customerService.getCustomerById(id)
            ?: throw CustomerNotFoundException("Customer with ID $id not found")
        customerService.deleteCustomer(customer)
        return ResponseEntity.ok("Customer deleted")
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<CustomerResponseDto>> {
        val customers = customerService.getAllCustomers()
        return ResponseEntity.ok(customers.map { it.toResponseDto() })
    }
}

