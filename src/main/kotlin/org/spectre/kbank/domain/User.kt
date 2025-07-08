package org.spectre.kbank.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.spectre.kbank.enums.UserRole

@Entity
@Table(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false,unique = true)
    val username: String,
    @Column(nullable = false)
    val password: String,
    @Enumerated(value = EnumType.STRING)
    val role: UserRole,
    @OneToOne
    @JoinColumn(name = "customer_id")
    val customer: Customer
)