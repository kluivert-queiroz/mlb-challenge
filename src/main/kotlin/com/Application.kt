package com

import io.micronaut.runtime.Micronaut.run
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*

@OpenAPIDefinition(
    info = Info(
        title = "mlb-challenge", version = "0.0", description = "Simple crud"
    )
)
object Api {}

fun main(args: Array<String>) {
    run(*args)
}

