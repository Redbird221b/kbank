package org.spectre.kbank.mapper

import org.spectre.kbank.domain.Customer
import org.spectre.kbank.dto.response.CustomerResponseDto

fun Customer.toResponseDto(): CustomerResponseDto {
    return CustomerResponseDto(
        id = this.id,
        name = this.name,
        email = this.email,
        phoneNumber = this.phoneNumber
    )
}
