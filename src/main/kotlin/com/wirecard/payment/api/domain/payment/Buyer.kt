package com.wirecard.payment.api.domain.payment

import com.fasterxml.jackson.annotation.JsonCreator
import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation
import com.wirecard.payment.api.infrastructure.hasLengthBetween
import com.wirecard.payment.api.infrastructure.hasMaxLengthOf
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

        if(name.isBlank() || !name.hasLengthBetween(3, 150))
            violations.add(Violation("Buyer's name cannot be empty and must be grater then 3 and lesser then 150 characters."))

        if(!email.isEmail() || !email.hasMaxLengthOf(100))
            violations.add(Violation("E-mail is not valid. Must has a valid e-mail format and be less then 100 characters."))

        if(violations.isNotEmpty()) throw ValidationException(violations)
    }

}







