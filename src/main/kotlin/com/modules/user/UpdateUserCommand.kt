package com.modules.user

import com.core.constraints.AdultAge
import com.fasterxml.jackson.annotation.JsonFormat
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Introspected
@Serdeable
data class UpdateUserCommand(
    @Size(min = 4, max = 50) @Schema(example = "John Doe") var name: String? = null,
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Pattern(
        regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$",
        message = "Value should be date in format yyyy-MM-dd."
    ) @AdultAge @Schema(example = "2000-01-01") var birthDate: String? = null
)