package com.wirecard.payment.api.domain

import com.wirecard.payment.api.domain.payment.PaymentRequest

interface CreditCardGateway {

    fun process(request: PaymentRequest): String
    fun check(ticket: String): PaymentRequest

}