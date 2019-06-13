package com.wirecard.payment.api.domain.payment

import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation

/**
 * Simple Interface to specify that an
 * Object can invoke the function validate()
 */
interface Validatable{

    /**
     * Must throw a ValidationException
     * when the object validated is not valid
     */
    fun validate()

}

/**
 * Aux function to collect validation violations
 * omitting a possible ValidationException
 */
fun Validatable.collectViolationsWithoutThrowing():List<Violation> =
        try {
            this.validate()
            emptyList()
        }catch (v:ValidationException){
            v.violations
        }