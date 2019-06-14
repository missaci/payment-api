package com.wirecard.payment.api.domain.payment

import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.ProcessState

interface CreditCardGateway {

    fun process(request: PaymentRequest): ProcessState
    fun check(request: PaymentRequest): ProcessState

}