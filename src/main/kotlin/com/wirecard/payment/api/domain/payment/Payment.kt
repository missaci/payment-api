package com.wirecard.payment.api.domain.payment

import com.fasterxml.jackson.annotation.JsonCreator
import com.wirecard.payment.api.domain.Validatable
import com.wirecard.payment.api.domain.collectViolationsWithoutThrowing
import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation

class Payment
@JsonCreator constructor(
        val amount: Double,
        val type: PaymentType,
        val card: Card? = null
) : Validatable {

    var boletoNumber:String? = null

    override fun validate() {
        val violations = mutableListOf<Violation>()

        if (amount <= 0) violations.add(Violation("Amount must be greater then 0."))
        if (PaymentType.CARD == type && card == null) violations.add(Violation("No card informed but payment is set as CARD."))
        if (PaymentType.BOLETO == type && card != null) violations.add(Violation("Card informed but payment is set as BOLETO."))
        if (PaymentType.CARD == type && card != null) violations.addAll(card.collectViolationsWithoutThrowing())

        if (violations.isNotEmpty()) throw ValidationException(violations)
    }

}

enum class PaymentType {
    BOLETO, CARD
}