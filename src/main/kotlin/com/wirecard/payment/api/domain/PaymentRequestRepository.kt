package com.wirecard.payment.api.domain

import com.wirecard.payment.api.domain.payment.PaymentRequest

interface PaymentRequestRepository {

    fun save(request:PaymentRequest): PaymentRequest

}