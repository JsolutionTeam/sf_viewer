package kr.co.jsol.common.util

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<ValidEnum, Enum<*>> {
    lateinit var annotation: ValidEnum
    override fun initialize(constraintAnnotation: ValidEnum) {
        annotation = constraintAnnotation
    }

    override fun isValid(value: Enum<*>, context: ConstraintValidatorContext): Boolean {
        var result = false
        val enumValues: Array<out Enum<*>>? = annotation.enumClass.java.enumConstants
        if (enumValues != null) {
            for (enumValue in enumValues) {
                if (value === enumValue) {
                    result = true
                    break
                }
            }
        }
        return result
    }
}
