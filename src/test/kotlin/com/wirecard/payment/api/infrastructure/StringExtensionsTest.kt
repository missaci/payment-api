package com.wirecard.payment.api.infrastructure

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StringExtensionsTest {

    @Test
    fun `should identify a correct natural number`(){
        assertTrue("12345".isNaturalNumber())
    }

    @Test
    fun `should not accept fractions as natural numbers`() {
        assertFalse("1234567.43".isNaturalNumber())
        assertFalse("1234567,43".isNaturalNumber())
    }

    @Test
    fun `should not identify as natural numbers mixed strings` () {
        assertFalse("23342A".isNaturalNumber())
    }

    @Test
    fun `should not identify as natural numbers null or empty values` () {
        assertFalse("".isNaturalNumber())
        assertFalse((null as String?).isNaturalNumber())
    }

}