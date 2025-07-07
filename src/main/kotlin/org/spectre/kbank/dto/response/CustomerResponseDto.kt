package org.spectre.kbank.dto.response

data class CustomerResponseDto(
    val id: Long?,
    val name: String,
    val email: String,
    val phoneNumber: String,
)
