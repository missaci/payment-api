package com.wirecard.payment.api.domain

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.wirecard.payment.api.domain.exceptions.PaymentRequestNotFoundException
import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.payment.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PaymentsTest {

    private val validBoletoPaymentRequest = PaymentRequest(
            clientId = 1,
            buyer = Buyer(name = "Mateus Missaci", email = "mateus@gmail.com", cpf = "081.356.740-82"),
            payment = Payment(amount = 10.0, type = PaymentType.BOLETO)
    )

    private val validCardPaymentRequest = PaymentRequest(
            clientId = 1,
            buyer = Buyer(name = "Mateus Missaci", email = "mateus@gmail.com", cpf = "081.356.740-82"),
            payment = Payment(amount = 10.0, type = PaymentType.CARD, card=Card(
                    holderName = "Mateus Missaci",
                    number = "5488 2607 0588 0201",
                    expirationDate = "10/20",
                    cvv = "355"
            ))
    )

    private val invalidPaymentRequest = PaymentRequest(
            clientId = 1,
            buyer = Buyer(name = "Mateus Missaci", email = "mateus@@gmail.com", cpf = "081.356.740-82"),
            payment = Payment(amount = 10.0, type = PaymentType.BOLETO)
    )

    private val boletoProvider = mock<BoletoProvider> {
        on { generateBoletoNumberFor(any()) } doReturn "testBoleto"
        on { check(validBoletoPaymentRequest)} doReturn ProcessState.ACCEPTED
    }

    private val creditCardGateway = mock<CreditCardGateway> {
        on { process(validCardPaymentRequest) } doReturn ProcessState.ACCEPTED
        on { check(validCardPaymentRequest)} doReturn ProcessState.ACCEPTED
    }

    private val repository = mock<PaymentRequestRepository> {
        on { save(validBoletoPaymentRequest) } doReturn validBoletoPaymentRequest
        on { save(validCardPaymentRequest) } doReturn validCardPaymentRequest
        on { find(validBoletoPaymentRequest.ticket) } doReturn validBoletoPaymentRequest
        on { find(validCardPaymentRequest.ticket) } doReturn validCardPaymentRequest
    }

    @Test
    fun `should process a boleto payment`() {
        val request = Payments(boletoProvider, creditCardGateway, repository).process(validBoletoPaymentRequest)

        assertNotNull(request)
        assertEquals("testBoleto", request.payment.boletoNumber)

    }

    @Test
    fun `should process a card payment`() {
        val request = Payments(boletoProvider, creditCardGateway, repository).process(validCardPaymentRequest)

        assertNotNull(request)
        assertEquals(ProcessState.ACCEPTED, request.status)

    }

    @Test
    fun `should reject an invalid payment request`() {
        val exception = assertThrows<ValidationException> {
            Payments(boletoProvider, creditCardGateway, repository).process(invalidPaymentRequest)
        }

        assertNotNull(exception)
        assertTrue(exception.message!!.contains("Validation failures occurred:"))

    }

    @Test
    fun `should check a valid boleto payment status`() {
        val request = Payments(boletoProvider, creditCardGateway, repository).checkState(validBoletoPaymentRequest.ticket)

        assertNotNull(request)
    }

    @Test
    fun `should check a valid card payment status`() {
        val request = Payments(boletoProvider, creditCardGateway, repository).checkState(validCardPaymentRequest.ticket)

        assertNotNull(request)
    }

    @Test
    fun `should throw a not found exception when a payment request ticket does not exists`() {
        val exception = assertThrows<PaymentRequestNotFoundException> {
            Payments(boletoProvider, creditCardGateway, repository).checkState(invalidPaymentRequest.ticket)
        }

        assertNotNull(exception)

    }

}