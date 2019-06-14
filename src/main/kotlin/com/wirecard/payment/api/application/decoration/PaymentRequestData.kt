package com.wirecard.payment.api.application.decoration

import com.fasterxml.jackson.annotation.JsonCreator
import com.wirecard.payment.api.domain.payment.Buyer
import com.wirecard.payment.api.domain.payment.PaymentRequest

/**
 * This class is intended to protect data submission
 * of some fields of PaymentRequest to the domain model.
 *
 * Must not be used inside domain
 */

class PaymentRequestData
@JsonCreator constructor(
        val clientId: Long,
        val buyer: Buyer,
        val payment: PaymentData
){
    fun toPaymentRequest() = PaymentRequest(clientId, buyer, payment.toPayment())
}