package com.wirecard.payment.api.domain.payment

import com.fasterxml.jackson.annotation.JsonCreator
import com.wirecard.payment.api.domain.Validatable
import com.wirecard.payment.api.domain.collectViolationsWithoutThrowing
import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation
import com.wirecard.payment.api.infrastructure.hasMinLengthOf
import com.wirecard.payment.api.infrastructure.isEmail

class Buyer
@JsonCreator constructor(
        val name: String,
        val email: String,
        cpf: String
) : Validatable {

    val cpf = CPF(cpf)

    override fun validate() {
        val violations = mutableListOf<Violation>()

        violations.addAll(cpf.collectViolationsWithoutThrowing())

        if(name.isBlank() || !name.hasMinLengthOf(3))
            violations.add(Violation("Buyer's name cannot be empty and must be grater then 3 characters."))

        if(!email.isEmail())
            violations.add(Violation("E-mail is not valid."))

        if(violations.isNotEmpty()) throw ValidationException(violations)
    }

}







