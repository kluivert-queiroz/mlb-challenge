package com.core.constraints

import com.util.CPF
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class CPFValidatorBeanTest(
    private val cpfValidatorBean: CPFValidatorBean
) : StringSpec({
    "should return true when CPF is valid" {
        val result = cpfValidatorBean.isValid(CPF.generate(), null, null)
        result.shouldBeTrue()
    }

    "should return false when CPF is invalid" {
        val result = cpfValidatorBean.isValid("12312312312", null, null)
        result.shouldBeFalse()
    }
})
