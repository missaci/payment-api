package com.wirecard.payment.api.infrastructure.providers

import com.wirecard.payment.api.domain.BoletoProvider
import com.wirecard.payment.api.domain.payment.PaymentRequest
import org.springframework.stereotype.Component

@Component
class MockedBoletoProvider : BoletoProvider {

    override fun generateBoletoNumberFor(request: PaymentRequest) =
            "10499.71201 22517.701235 45678.901518 1 8424" +
                    "${request.payment.amount.toString().padStart(10, '0')}"

}