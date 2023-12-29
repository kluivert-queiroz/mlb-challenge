package com.core.constraints

import jakarta.validation.Constraint

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AdultAgeValidatorBean::class])
annotation class AdultAge (
    val message: String = "Invalid date. The user must be over 18 years old."
)