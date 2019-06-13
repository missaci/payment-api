package com.wirecard.payment.api.infrastructure

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

class DateExtensionsTest {

    @Test
    fun `should format a date according to a given pattern`() {
        val pattern = "MM/yyyy"
        val date = Date()
        val defaultFormatter = SimpleDateFormat(pattern)

        assertEquals(defaultFormatter.format(date), date.format(pattern))
    }

    @Test
    fun `should return null when given date is null`() {
        val pattern = "MM/yyyy"
        val date:Date? = null

        assertEquals(null, date.format(pattern))
    }


}