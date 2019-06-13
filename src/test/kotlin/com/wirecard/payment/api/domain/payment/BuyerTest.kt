package com.wirecard.payment.api.domain.payment

import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BuyerTest {

    @Test
    fun `should create a valid Buyer`() {
        val buyer = Buyer(
                name = "Mateus Missaci",
                email = "mateus.missaci@gmail.com",
                cpf = "081.356.740-82"
        )

        buyer.validate()

        assertEquals("Mateus Missaci", buyer.name)
        assertEquals("mateus.missaci@gmail.com", buyer.email)
        assertEquals("081.356.740-82", buyer.cpf.value)

    }

    @Test
    fun `should accept different formats of CPF`() {
        listOf(
                "081.356.740-82",
                "081.356.74082",
                "08135674082",
                "081356740-82"

        ).forEach { cpf ->
            val buyer = Buyer(
                    name = "Mateus Missaci",
                    email = "mateus.missaci@gmail.com",
                    cpf = cpf
            )

            buyer.validate()

            assertEquals("Mateus Missaci", buyer.name)
            assertEquals("mateus.missaci@gmail.com", buyer.email)
            assertEquals(cpf, buyer.cpf.value)
        }

    }

    @Test
    fun `should reject invalid CPFs`() {
        listOf(
                "081.356.740-83",
                "081.356.7408C",
                "08135675082",
                "081-356-740.82",
                "081.356.740/82",
                "0081.356.740-82",
                "81.356.740-82"

        ).forEach { cpf ->
            val exception = assertThrows(ValidationException::class.java) {
                Buyer(
                        name = "Mateus Missaci",
                        email = "mateus.missaci@gmail.com",
                        cpf = cpf
                ).validate()
            }

            assertTrue(exception.violations.size == 1)
            assertTrue(exception.violations.first().message == CPF_VALIDATION_MESSAGE + cpf)

        }

    }

    @Test
    fun `should reject invalid emails`() {
        listOf(
                "mateus@",
                "@com.br",
                "ma#teus@teste.com.br",
                "mat&eus@teste.com.br",
                "mateus@com"
        ).forEach { email ->
            val exception = assertThrows(ValidationException::class.java) {
                Buyer(
                        name = "Mateus Missaci",
                        email = email,
                        cpf = "081.356.740-82"
                ).validate()
            }

            assertTrue(exception.violations.size == 1)
            assertTrue(exception.violations.first().message == EMAIL_VALIDATION_MESSAGE)

        }

    }

    @Test
    fun `should accept valid emails`() {
        listOf(
                "mateus@uol.com.br",
                "mateus.missaci@uol.com",
                "mateus_missaci@teste.com.br",
                "mateus-missaci@subserver.teste.com.br"
        ).forEach { email ->
            val buyer = Buyer(
                    name = "Mateus Missaci",
                    email = email,
                    cpf = "081.356.740-82"
            )

            buyer.validate()

            assertEquals("Mateus Missaci", buyer.name)
            assertEquals(email, buyer.email)
            assertEquals("081.356.740-82", buyer.cpf.value)

        }
    }

    @Test
    fun `should not accept invalid names`() {
        val exception = assertThrows(ValidationException::class.java) {
            Buyer(
                    name = "Ma",
                    email = "mateus.missaci@gmail.com",
                    cpf = "081.356.740-82"
            ).validate()
        }

        assertTrue(exception.violations.size == 1)
        assertTrue(exception.violations.first().message == NAME_VALIDATION_MESSAGE)

    }

    @Test
    fun `should not accept empty fields`() {
        val exception = assertThrows(ValidationException::class.java) {
            Buyer(
                    name = "",
                    email = "",
                    cpf = ""
            ).validate()
        }

        assertTrue(exception.violations.map(Violation::message).contains(NAME_VALIDATION_MESSAGE))
        assertTrue(exception.violations.map(Violation::message).contains(EMAIL_VALIDATION_MESSAGE))
        assertTrue(exception.violations.map(Violation::message).contains(CPF_VALIDATION_MESSAGE))

    }

    companion object {
        private const val NAME_VALIDATION_MESSAGE = "Buyer's name cannot be empty and must be grater then 3 and lesser then 150 characters."
        private const val EMAIL_VALIDATION_MESSAGE = "E-mail is not valid. Must has a valid e-mail format and be less then 100 characters."
        private const val CPF_VALIDATION_MESSAGE = "CPF informed is not valid: "

    }

}