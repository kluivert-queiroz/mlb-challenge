package com.core.constraints

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import java.sql.Date
import java.time.LocalDate

@MicronautTest()
class AdultAgeValidatorBeanTest(
    private val adultAgeValidatorBean: AdultAgeValidatorBean
) : StringSpec({
    "should return true when date is after 18 years" {
        com.core.Date.setNow(LocalDate.of(2020, 1, 1))
        val result = adultAgeValidatorBean.isValid(Date.valueOf("2000-01-01"), null, null)
        result.shouldBeTrue()
    }

    "should return false when date is before 18 years" {
        com.core.Date.setNow(LocalDate.of(2020, 1, 1))
        val result = adultAgeValidatorBean.isValid(Date.valueOf("2010-01-01"), null, null)
        result.shouldBeFalse()
    }
})