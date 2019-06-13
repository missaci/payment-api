package com.wirecard.payment.api.application.exceptions.handler

import org.springframework.http.HttpStatus


data class ApiExceptionMessage(
        val status: HttpStatus,
        val message: List<String>
)
