package com.core.constraints

import jakarta.validation.Constraint

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
annotation class CPF(
    val message: String = "Invalid CPF. It must have 11 digits without punctuation."
)