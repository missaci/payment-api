package com.wirecard.payment.api.domain.payment

import com.wirecard.payment.api.domain.payment.PaymentRequest

interface PaymentRequestRepository {

    fun save(request:PaymentRequest): PaymentRequest
    fun find(ticket: String): PaymentRequest?
}