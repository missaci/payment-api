package com.wirecard.payment.api.domain

import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.PaymentType.*
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class Payments
@Autowired constructor(
        private val boletoProvider: BoletoProvider,
        private val creditCardGateway: CreditCardGateway,
        private val repository: PaymentRequestRepository
) {

    fun process(request: PaymentRequest): Publisher<PaymentRequest> {
        request.validate()

        return Mono.just(processPayment(request))

    }

    private fun processPayment(request: PaymentRequest):PaymentRequest {
        when (request.payment.type) {
            BOLETO -> request.payment.boletoNumber = boletoProvider.generateBoletoNumberFor(request)
            CARD -> request.status = creditCardGateway.process(request)
        }

        return repository.save(request)
    }

}