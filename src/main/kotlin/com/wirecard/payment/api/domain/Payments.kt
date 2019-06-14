package com.wirecard.payment.api.domain

import com.wirecard.payment.api.domain.exceptions.PaymentRequestNotFoundException
import com.wirecard.payment.api.domain.payment.BoletoProvider
import com.wirecard.payment.api.domain.payment.CreditCardGateway
import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.PaymentRequestRepository
import com.wirecard.payment.api.domain.payment.PaymentType.BOLETO
import com.wirecard.payment.api.domain.payment.PaymentType.CARD
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Payments
@Autowired constructor(
        private val boletoProvider: BoletoProvider,
        private val creditCardGateway: CreditCardGateway,
        private val repository: PaymentRequestRepository
) {

    fun process(request: PaymentRequest): PaymentRequest {
        request.validate()

        when (request.payment.type) {
            BOLETO -> request.payment.boletoNumber = boletoProvider.generateBoletoNumberFor(request)
            CARD -> request.status = creditCardGateway.process(request)
        }

        return repository.save(request)

    }

    fun checkState(ticket: String): PaymentRequest {
        val request = repository.find(ticket)
                ?: throw PaymentRequestNotFoundException(ticket)

        when (request.payment.type) {
            CARD -> request.status = creditCardGateway.check(request)
            BOLETO -> request.status = boletoProvider.check(request)
        }

        return request
    }

}