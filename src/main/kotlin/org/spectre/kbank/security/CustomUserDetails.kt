package org.spectre.kbank.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.spectre.kbank.domain.User
import org.springframework.security.core.authority.SimpleGrantedAuthority

class CustomUserDetails(
    private val user: User
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?>? = listOf(SimpleGrantedAuthority(user.role.toString()))

    override fun getPassword(): String? = user.password

    override fun getUsername(): String? = user.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}