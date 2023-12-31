package com.modules.user

import com.core.constraints.AdultAge
import com.core.constraints.CPF
import com.fasterxml.jackson.annotation.JsonFormat
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import java.time.LocalDate

@Introspected
@Serdeable
data class CreateUserCommand(
    @NotNull @NotBlank @Email @Schema(example = "john.doe@test.com") val email: String,
    @NotNull @NotBlank @CPF @Schema(example = "47277852001") val cpf: String,
    @NotNull @NotBlank @Size(min = 4, max = 50) @Schema(name = "John Doe") val name: String,
    @NotNull @Pattern(
        regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$",
        message = "Value should be date in format yyyy-MM-dd."
    ) @AdultAge @Schema(example = "2000-01-01") val birthDate: String
)