package org.spectre.kbank.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class Customer (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val phoneNumber: String,
    val email: String,
    @OneToMany(mappedBy = "accountHolder", cascade = [CascadeType.ALL], orphanRemoval = true)
    val accounts: MutableList<BankAccount> = mutableListOf()
)