package com.wirecard.payment.api.application.decoration

import com.fasterxml.jackson.annotation.JsonCreator
import com.wirecard.payment.api.domain.payment.Card
import com.wirecard.payment.api.domain.payment.Payment
import com.wirecard.payment.api.domain.payment.PaymentType

/**
 * This class is intended to protect data submission
 * of some fields of Payment to the domain model
 *
 *  Must not be used inside domain
 */
class PaymentData
@JsonCreator constructor(
        val amount: Double,
        val type: PaymentType,
        val card: Card? = null
) {

    fun toPayment() = Payment(amount, type, card)

}