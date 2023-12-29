package com.core.http

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

@Introspected
@Serdeable
data class ErrorResponse(
    @Schema(example = "bad_request")
    val code: String,
    @Schema(example = "Invalid request")
    val message: String,
    val fields: List<FieldError>? = null
)

@Introspected
@Serdeable
data class FieldError(
    @Schema(example = "email")
    val field: String,
    @Schema(example = "must be a well-formed email address")
    val message: String
)