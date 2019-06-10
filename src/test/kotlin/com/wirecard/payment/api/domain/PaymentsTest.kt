package com.wirecard.payment.api.domain

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.wirecard.payment.api.domain.payment.Buyer
import com.wirecard.payment.api.domain.payment.Payment
import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.PaymentType
import org.junit.jupiter.api.Assertions.*
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
            on { process(any()) } doReturn paymentRequest.ticket
        }

        val ticket = Payments(boletoProvider, creditCardGateway).process(paymentRequest)

        assertNotNull(ticket)
        assertEquals("testBoleto", ticket)

    }

}