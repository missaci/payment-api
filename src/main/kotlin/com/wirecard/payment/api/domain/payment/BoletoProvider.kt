package com.wirecard.payment.api.domain.payment

import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.ProcessState

interface BoletoProvider{

    fun generateBoletoNumberFor(request: PaymentRequest): String
    fun check(request: PaymentRequest): ProcessState

}