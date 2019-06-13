package com.wirecard.payment.api.domain.payment

import com.fasterxml.jackson.annotation.JsonCreator
import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation
import java.util.*

class PaymentRequest
@JsonCreator constructor(
        val clientId: Long,
        val buyer: Buyer,
        val payment: Payment,
        var status: ProcessState = ProcessState.PENDING,
        val ticket: String = UUID.randomUUID().toString()
) : Validatable {

    override fun validate() {
        val violations = mutableListOf<Violation>()

        if (clientId <= 0) violations.add(Violation("ClientId invalid."))
        violations.addAll(buyer.collectViolationsWithoutThrowing())
        violations.addAll(payment.collectViolationsWithoutThrowing())

        if (violations.isNotEmpty()) throw ValidationException(violations)
    }

}

enum class ProcessState {

    PENDING, ACCEPTED, REJECTED

}