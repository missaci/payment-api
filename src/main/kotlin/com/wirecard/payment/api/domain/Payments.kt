package com.wirecard.payment.api.domain

import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.PaymentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Payments
@Autowired constructor(
        private val boletoProvider: BoletoProvider,
        private val creditCardGateway: CreditCardGateway
) {

    fun process(request: PaymentRequest): String {
        request.validate()

        return when (request.payment.type) {
            PaymentType.BOLETO -> boletoProvider.generateBoletoNumberFor(request)
            PaymentType.CARD -> creditCardGateway.process(request)
        }

    }

}