package com.wirecard.payment.api.domain

import com.wirecard.payment.api.domain.payment.PaymentRequest

interface BoletoProvider{

    fun generateBoletoNumberFor(request: PaymentRequest): String

}