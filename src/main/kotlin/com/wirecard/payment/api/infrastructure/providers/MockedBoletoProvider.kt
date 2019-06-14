package com.wirecard.payment.api.infrastructure.providers

import com.wirecard.payment.api.domain.payment.BoletoProvider
import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.ProcessState
import org.springframework.stereotype.Component

@Component
class MockedBoletoProvider : BoletoProvider {
    override fun check(request: PaymentRequest): ProcessState {
        return ProcessState.ACCEPTED
    }

    override fun generateBoletoNumberFor(request: PaymentRequest): String {
        val value = "%.2f".format(request.payment.amount)
                .replace("[.,]".toRegex(), "")
                .padStart(10, '0')
        return "10499.71201 22517.701235 45678.901518 1 8424$value"

    }

}