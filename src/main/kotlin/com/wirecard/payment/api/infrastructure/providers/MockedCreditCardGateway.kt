package com.wirecard.payment.api.infrastructure.providers

import com.wirecard.payment.api.domain.payment.CreditCardGateway
import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.ProcessState
import org.springframework.stereotype.Component

@Component
class MockedCreditCardGateway : CreditCardGateway {

    override fun check(request: PaymentRequest) =
            if(request.payment.amount.toInt() % 2 == 0)
                ProcessState.ACCEPTED
            else
                ProcessState.REJECTED

    override fun process(request: PaymentRequest): ProcessState = ProcessState.PENDING

}