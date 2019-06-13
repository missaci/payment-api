package com.wirecard.payment.api.application.exceptions.handler

import com.wirecard.payment.api.domain.exceptions.PaymentRequestNotFoundException
import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice(basePackages = ["com.wirecard.payment.api"])
class ExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleHttpMessageNotReadable(
            ex: HttpMessageNotReadableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ) = buildResponseEntity(
            ApiExceptionMessage(
                    HttpStatus.BAD_REQUEST,
                    listOf("Malformed JSON request")
            )
    )


    override fun handleMethodArgumentNotValid(
            ex: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        val errors = ex.bindingResult
                .allErrors
                .map {
                    val fieldName = (it as FieldError).field
                    "$fieldName: ${it.defaultMessage}"
                }

        return buildResponseEntity(
                ApiExceptionMessage(
                        HttpStatus.PRECONDITION_FAILED,
                        errors
                )
        )
    }

    @ExceptionHandler(ValidationException::class)
    fun validationException(exception:ValidationException): ResponseEntity<Any> {
        return buildResponseEntity(
                ApiExceptionMessage(
                        HttpStatus.PRECONDITION_FAILED,
                        exception.violations.map(Violation::message)
                )
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityViolationException() =
            buildResponseEntity(
                    ApiExceptionMessage(
                            HttpStatus.CONFLICT,
                            listOf("Data Integrity violation has happened during your request.")
                    )
            )

    @ExceptionHandler(PaymentRequestNotFoundException::class)
    fun notFoundException() = buildResponseEntity(
            ApiExceptionMessage(
                    HttpStatus.NOT_FOUND,
                    listOf("Given ticket was not found as a payment request.")
            )
    )


    private fun buildResponseEntity(apiMessage: ApiExceptionMessage): ResponseEntity<Any> =
            ResponseEntity(apiMessage, apiMessage.status)
}
