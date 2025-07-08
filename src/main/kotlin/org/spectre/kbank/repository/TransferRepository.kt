package org.spectre.kbank.repository

import org.spectre.kbank.domain.Transfer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransferRepository: JpaRepository<Transfer, Long>