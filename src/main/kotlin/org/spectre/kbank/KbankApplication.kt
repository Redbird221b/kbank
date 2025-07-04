package org.spectre.kbank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KbankApplication

fun main(args: Array<String>) {
    runApplication<KbankApplication>(*args)
}
