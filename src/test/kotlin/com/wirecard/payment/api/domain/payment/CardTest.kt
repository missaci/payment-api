package com.wirecard.payment.api.domain.payment

import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat

class CardTest {

    @Test
    fun `should create a valid card`() {
        val card = Card(holderName = "Mateus Missaci",
                number = "5488 2607 0588 0201",
                expirationDate = "10/20",
                cvv = "355")

        card.validate()

        assertEquals("Mateus Missaci", card.holderName)
        assertEquals("5488 2607 0588 0201", card.number)
        assertEquals("10/20", card.expirationDate.value)
        assertEquals("31/10/2020", SimpleDateFormat("dd/MM/yyyy").format(card.expirationDate.toDate()))
        assertEquals("355", card.cvv)
    }

    @Test
    fun `should accept hyphen and spaces optionally on card number`() {
        listOf(
                "5488 2607 0588 0201",
                "5488260705880201",
                "5488-2607-0588-0201",
                "54882607-05880201"
        ).forEach { cardNumber ->
            val card = Card(holderName = "Mateus Missaci",
                    number = cardNumber,
                    expirationDate = "10/20",
                    cvv = "355")

            card.validate()

            assertEquals("Mateus Missaci", card.holderName)
            assertEquals(cardNumber, card.number)
            assertEquals("10/20", card.expirationDate.value)
            assertEquals("31/10/2020", SimpleDateFormat("dd/MM/yyyy").format(card.expirationDate.toDate()))
            assertEquals("355", card.cvv)

        }

    }

    @Test
    fun `should throw exception if name length less then 3`() {
        val exception = assertThrows(ValidationException::class.java) {
            Card(holderName = "Ma",
                    number = "5488 2607 0588 0201",
                    expirationDate = "10/20",
                    cvv = "355")
                    .validate()
        }

        assertTrue(exception.violations.size == 1 && exception.violations.first().message == HOLDER_VALIDATION_MESSAGE)
    }

    @Test
    fun `should throw exception if card number is not valid`() {
        listOf(
                "5488 2607 0588 0202",
                "5488 2607",
                "5488 2607 0588 020A",
                "5488/2607/0588/0201"
        ).forEach { cardNumber ->

            val exception = assertThrows(ValidationException::class.java) {
                Card(holderName = "Mateus Missaci",
                        number = cardNumber,
                        expirationDate = "10/20",
                        cvv = "355")
                        .validate()
            }

            assertTrue(exception.violations.size == 1 && exception.violations.first().message == NUMBER_VALIDATION_MESSAGE)

        }
    }

    @Test
    fun `should throw exception if expiration date has passed`() {
        val exception = assertThrows(ValidationException::class.java) {
            Card(holderName = "Mateus Missaci",
                    number = "5488 2607 0588 0201",
                    expirationDate = "10/18",
                    cvv = "355")
                    .validate()
        }

        assertTrue(exception.violations.size == 1 &&
                exception.violations.first().message == CARD_DATE_EXPIRED_VALIDATION_MESSAGE + "10/18")
    }

    @Test
    fun `should accept all basic date formats`() {
        listOf(
                "09/20",
                "09/2020",
                "9/20",
                "9/2020"
        ).forEach { date ->
            val card = Card(holderName = "Mateus Missaci",
                    number = "5488 2607 0588 0201",
                    expirationDate = date,
                    cvv = "355")

            card.validate()

            assertEquals("Mateus Missaci", card.holderName)
            assertEquals("5488 2607 0588 0201", card.number)
            assertEquals(date, card.expirationDate.value)
            assertEquals("30/09/2020", SimpleDateFormat("dd/MM/yyyy").format(card.expirationDate.toDate()))
            assertEquals("355", card.cvv)

        }
    }

    @Test
    fun `should reject unknown date formats`() {
        listOf(
                "09-20",
                "09-2020",
                "920",
                "92020",
                "10/10/2020",
                "10/10/20"
        ).forEach { date ->
            val exception = assertThrows(ValidationException::class.java) {
                Card(holderName = "Mateus Missaci",
                        number = "5488 2607 0588 0201",
                        expirationDate = date,
                        cvv = "355")
                        .validate()
            }

            assertTrue(exception.violations.size == 1 &&
                    exception.violations.first().message == CARD_DATE_FORMAT_VALIDATION_MESSAGE + date)

        }
    }

    @Test
    fun `should accept cvv and cvc according to length`() {
        listOf(
                "352",
                "4333"
        ).forEach { cvv ->
            val card = Card(holderName = "Mateus Missaci",
                    number = "5488 2607 0588 0201",
                    expirationDate = "10/20",
                    cvv = cvv)

            card.validate()

            assertEquals("Mateus Missaci", card.holderName)
            assertEquals("5488 2607 0588 0201", card.number)
            assertEquals("10/20", card.expirationDate.value)
            assertEquals("31/10/2020", SimpleDateFormat("dd/MM/yyyy").format(card.expirationDate.toDate()))
            assertEquals(cvv, card.cvv)

        }
    }

    @Test
    fun `should reject unknown cvv format`() {
        listOf(
                "20",
                "1",
                "32123",
                "a23",
                "3-1",
                "2 2"
        ).forEach { cvv ->
            val exception = assertThrows(ValidationException::class.java) {
                Card(holderName = "Mateus Missaci",
                        number = "5488 2607 0588 0201",
                        expirationDate = "10/20",
                        cvv = cvv)
                        .validate()
            }


            assertTrue(exception.violations.size == 1 &&
                    exception.violations.first().message == CVV_VALIDATION_MESSAGE)

        }
    }

    @Test
    fun `should throw an Exception containing all empty fields`() {
        val exception = assertThrows(ValidationException::class.java) {
            Card(holderName = "", number = "", expirationDate = "", cvv = "")
                    .validate()
        }

        assertTrue(exception.violations.map(Violation::message).contains(HOLDER_VALIDATION_MESSAGE))
        assertTrue(exception.violations.map(Violation::message).contains(NUMBER_VALIDATION_MESSAGE))
        assertTrue(exception.violations.map(Violation::message).contains(CARD_DATE_FORMAT_VALIDATION_MESSAGE))
        assertTrue(exception.violations.map(Violation::message).contains(CVV_VALIDATION_MESSAGE))
    }

    companion object {
        private const val HOLDER_VALIDATION_MESSAGE = "Card holder name cannot be empty and must have length of 3 or more characters."
        private const val NUMBER_VALIDATION_MESSAGE = "Card number is not valid."
        private const val CARD_DATE_FORMAT_VALIDATION_MESSAGE = "Date could not be parsed: "
        private const val CARD_DATE_EXPIRED_VALIDATION_MESSAGE = "Card date expired: "
        private const val CVV_VALIDATION_MESSAGE = "Invalid cvv format."
    }
}
