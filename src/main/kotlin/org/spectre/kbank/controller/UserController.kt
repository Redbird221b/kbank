package org.spectre.kbank.controller

import org.spectre.kbank.annotation.CurrentUser
import org.spectre.kbank.domain.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping("/me")
    fun getCurrentUser(@CurrentUser user: User): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, ${user.username} with role ${user.role}")
    }
}
