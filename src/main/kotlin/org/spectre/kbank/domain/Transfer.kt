package org.spectre.kbank.domain

import jakarta.persistence.Entity
//import jakarta.persistence.EnumType
//import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
//import org.spectre.kbank.enums.TransferStatus
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class Transfer (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "from_account_id", nullable = false)
    val fromAccount: BankAccount,
    @ManyToOne
    @JoinColumn(name = "to_account_id", nullable = false)
    val toAccount: BankAccount,
    val amount: BigDecimal,
    val createdAt: LocalDateTime,
//    @Enumerated(EnumType.STRING)
//    val status: TransferStatus,
    val description: String? = null,
    @ManyToOne
    @JoinColumn(name = "initiated_by")
    val initiatedBy: Customer? = null,
    val transactionIdFrom: Long? = null,
    val transactionIdTo: Long? = null
)