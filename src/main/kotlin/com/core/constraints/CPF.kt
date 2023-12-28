package com.core.constraints

import jakarta.validation.Constraint

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
annotation class CPF(
    val message: String = "invalid CPF {{validatedValue}}"
)