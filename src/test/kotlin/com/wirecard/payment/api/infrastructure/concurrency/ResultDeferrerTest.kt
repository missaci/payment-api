package com.wirecard.payment.api.infrastructure.concurrency

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ResultDeferrerTest {

    @Test
    fun `should create a valid DefferedResult Object`() {

        val result = defer {
            "Result"
        }

        result.setResultHandler {
            assertTrue(result.hasResult())
            assertEquals("Result", result.result)
        }

    }

    @Test
    fun `should forward exceptions when then occur`() {

        val result = defer {
            throw RuntimeException("Test")
        }

        result.onError {
            assertEquals("Test", it.message)

        }

    }

}