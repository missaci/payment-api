package com.wirecard.payment.api.domain.payment

import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PaymentTest {

    @Test
    fun `should create a valid payment`() {
        val payment = Payment(
                amount = 10.0,
                type = PaymentType.CARD,
                card = VALID_CARD
        )

        payment.validate()

        assertEquals(10.0, payment.amount)
        assertEquals(PaymentType.CARD, payment.type)
        assertEquals("Mateus Missaci", payment.card?.holderName)
        assertEquals("5488 2607 0588 0201", payment.card?.number)
        assertEquals("10/20", payment.card?.expirationDate?.value)
        assertEquals("355", payment.card?.cvv)

    }

    @Test
    fun `should reject values lesser or equal 0`() {
        listOf(
                0.0,
                -1.0
        ).forEach { amount ->
            val exception = assertThrows(ValidationException::class.java) {
                Payment(
                        amount = amount,
                        type = PaymentType.CARD,
                        card = VALID_CARD
                ).validate()
            }

            assertTrue(exception.violations.size == 1 && exception.violations.first().message == AMOUNT_VALIDATION_MESSAGE)

        }
    }

    @Test
    fun `should reject payments of type CARD without a card`() {
        val exception = assertThrows(ValidationException::class.java) {
            Payment(
                    amount = 10.0,
                    type = PaymentType.CARD
            ).validate()
        }

        assertTrue(exception.violations.size == 1 && exception.violations.first().message == NO_CARD_VALIDATION_MESSAGE)

    }

    @Test
    fun `should accept payments of type BOLETO without a card`() {
        val payment = Payment(
                amount = 10.0,
                type = PaymentType.BOLETO
        )

        payment.validate()

        assertEquals(10.0, payment.amount)
        assertEquals(PaymentType.BOLETO, payment.type)
        assertNull(payment.card)

    }

    @Test
    fun `should reject payments of type BOLETO if a card was informed`() {
        val exception = assertThrows(ValidationException::class.java) {
            Payment(
                    amount = 10.0,
                    type = PaymentType.BOLETO,
                    card = VALID_CARD
            ).validate()
        }

        assertTrue(exception.violations.size == 1 && exception.violations.first().message == CARD_INFORMED_ON_BOLETO_VALIDATION_MESSAGE)

    }

    @Test
    fun `should inform multiple failures at once when payment is BOLETO`() {
        val exception = assertThrows(ValidationException::class.java) {
            Payment(
                    amount = 0.0,
                    type = PaymentType.BOLETO,
                    card = VALID_CARD
            ).validate()
        }

        assertTrue(exception.violations.map(Violation::message).contains(AMOUNT_VALIDATION_MESSAGE))
        assertTrue(exception.violations.map(Violation::message).contains(CARD_INFORMED_ON_BOLETO_VALIDATION_MESSAGE))

    }

    @Test
    fun `should inform multiple failures at once when payment is CARD`() {
        val exception = assertThrows(ValidationException::class.java) {
            Payment(
                    amount = 0.0,
                    type = PaymentType.CARD
            ).validate()
        }

        assertTrue(exception.violations.map(Violation::message).contains(AMOUNT_VALIDATION_MESSAGE))
        assertTrue(exception.violations.map(Violation::message).contains(NO_CARD_VALIDATION_MESSAGE))

    }

    companion object {
        private const val AMOUNT_VALIDATION_MESSAGE = "Amount must be greater then 0."
        private const val NO_CARD_VALIDATION_MESSAGE = "No card informed but payment is set as CARD."
        private const val CARD_INFORMED_ON_BOLETO_VALIDATION_MESSAGE = "Card informed but payment is set as BOLETO."

        private val VALID_CARD = Card(
                holderName = "Mateus Missaci",
                number = "5488 2607 0588 0201",
                expirationDate = "10/20",
                cvv = "355"
        )

    }

}