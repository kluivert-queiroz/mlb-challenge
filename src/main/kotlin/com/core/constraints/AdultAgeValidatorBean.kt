package com.core.constraints

import com.core.Date
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton
import java.time.LocalDate
import java.time.LocalDateTime

@Singleton
@Introspected
class AdultAgeValidatorBean : ConstraintValidator<AdultAge, Any> {

    override fun isValid(
        value: Any?,
        annotationMetadata: AnnotationValue<AdultAge>?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if(value == null) return true
        if (value.toString().isEmpty()) return false;
        val birthDate = value.toString().split("-").map { it.toInt() }
        val today = Date.now()
        val age = today.year - birthDate[0]
        return if (today.monthValue < birthDate[1]) age - 1 >= 18 else age >= 18
    }
}