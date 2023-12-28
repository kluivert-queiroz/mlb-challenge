package com.modules.user

import com.core.constraints.CPF
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Introspected
@Serdeable
data class CreateUserCommand(
    @NotNull @NotBlank @Email val email: String,
    @NotNull @NotBlank @CPF val cpf: String,
    @NotNull @NotBlank val name: String,
)