package com.wirecard.payment.api.domain

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.wirecard.payment.api.domain.payment.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class PaymentsTest {

    @Test
    fun `should process a payment`() {

        val paymentRequest = PaymentRequest(
                clientId = 1,
                buyer = Buyer(name = "Mateus Missaci", email = "mateus@gmail.com", cpf = "081.356.740-82"),
                payment = Payment(amount = 10.0, type = PaymentType.BOLETO)
        )

        val boletoProvider = mock<BoletoProvider> {
            on { generateBoletoNumberFor(any()) } doReturn "testBoleto"
        }

        val creditCardGateway = mock<CreditCardGateway> {
            on { process(any()) } doReturn ProcessState.PENDING
        }

        val repository = mock<PaymentRequestRepository> {
            on { save(paymentRequest) } doReturn paymentRequest
        }

        val request = Payments(boletoProvider, creditCardGateway, repository).process(paymentRequest)

        assertNotNull(request)
        assertEquals("testBoleto", request.payment.boletoNumber)

    }

}