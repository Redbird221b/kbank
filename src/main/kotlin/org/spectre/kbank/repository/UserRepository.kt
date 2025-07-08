package org.spectre.kbank.repository

import org.spectre.kbank.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String?): User?
}