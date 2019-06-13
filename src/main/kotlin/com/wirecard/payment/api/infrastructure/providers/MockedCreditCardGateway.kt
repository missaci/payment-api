package com.wirecard.payment.api.infrastructure.providers

import com.wirecard.payment.api.domain.CreditCardGateway
import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.ProcessState
import org.springframework.stereotype.Component

@Component
class MockedCreditCardGateway : CreditCardGateway {

    override fun check(request: PaymentRequest) = ProcessState.ACCEPTED

    override fun process(request: PaymentRequest): ProcessState = ProcessState.PENDING

}