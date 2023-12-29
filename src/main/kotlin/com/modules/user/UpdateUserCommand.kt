package com.modules.user

import com.core.constraints.AdultAge
import com.fasterxml.jackson.annotation.JsonFormat
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

@Introspected
@Serdeable
data class UpdateUserCommand(
    var name: String? = null,
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Pattern(
        regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$",
        message = "Value should be date in format yyyy-MM-dd."
    ) @AdultAge var birthDate: String? = null
)