package com

import io.micronaut.runtime.Micronaut.run
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*

@OpenAPIDefinition(
    info = Info(
        title = "Mercado Livre Brasil Challenge", version = "0.0", description = "Simple crud for user management"
    )
)
object Api {}

fun main(args: Array<String>) {
    println("Enviroment ${System.getenv("MICRONAUT_ENVIRONMENTS")}")
    run(*args)
}

