package com.core.constraints

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton

@Singleton
@Introspected
class CPFValidatorBean : ConstraintValidator<CPF, Any> {
    override fun isValid(
        value: Any?, annotationMetadata: AnnotationValue<CPF>?, context: ConstraintValidatorContext?
    ): Boolean {
        if (value.toString().isEmpty()) return false;
        val numbers = value.toString().filter { it.isDigit() }.map { it.toString().toInt() }
        if (numbers.size != 11) return false
        if (numbers.all { it == numbers[0] }) return false

        val dv1 = ((0..8).sumOf { (it + 1) * numbers[it] }).rem(11).let {
            if (it >= 10) 0 else it
        }
        val dv2 = ((0..8).sumOf { it * numbers[it] }.let { (it + (dv1 * 9)).rem(11) }).let {
            if (it >= 10) 0 else it
        }

        return numbers[9] == dv1 && numbers[10] == dv2
    }

}