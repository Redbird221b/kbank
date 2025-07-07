package org.spectre.kbank.service

import org.spectre.kbank.domain.Customer
import org.spectre.kbank.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerService @Autowired constructor(
    private val repository: CustomerRepository
) {

    fun findCustomerByEmail(email: String): Customer? {
        val customers = repository.findAll()
        return customers.firstOrNull { it.email == email }
    }

    fun findCustomerByPhoneNumber(phoneNumber: String): Customer? {
        val customers = repository.findAll()
        return customers.firstOrNull { it.phoneNumber == phoneNumber }
    }

    fun getCustomerById(id: Long): Customer? {
        return repository.findById(id).orElse(null)
    }

    fun createCustomer(name: String, phoneNumber: String, email: String): Customer {
        if (findCustomerByEmail(email) != null || findCustomerByPhoneNumber(phoneNumber) != null) {
            throw IllegalArgumentException("Customer with email $email already exists")
        } else {
            val customer = Customer(name = name, phoneNumber = phoneNumber, email = email)
            return repository.save(customer)
        }
    }

    fun deleteCustomer(customer: Customer) {
        repository.delete(customer)
    }

}