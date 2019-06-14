package com.wirecard.payment.api.application

import com.wirecard.payment.api.application.decoration.PaymentRequestData
import com.wirecard.payment.api.application.exceptions.handler.ApiExceptionMessage
import com.wirecard.payment.api.domain.Payments
import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.ProcessState
import com.wirecard.payment.api.infrastructure.concurrency.defer
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/payments")
class PaymentController
@Autowired constructor(private val payments: Payments) {

    @ApiOperation(
            value = "Creates a new payment request process",
            response = RequestState::class,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            ApiResponse(code = 201, message = "Payment request created successfully"),
            ApiResponse(code = 400, message = "Bad Request", response = ApiExceptionMessage::class),
            ApiResponse(code = 409, message = "Data Integrity Violation", response = ApiExceptionMessage::class),
            ApiResponse(code = 412, message = "Validation failure", response = ApiExceptionMessage::class)
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody data: PaymentRequestData, httpRequest: HttpServletRequest) =
            defer {
                val paymentRequest = data.toPaymentRequest()
                val uri = httpRequest.requestURL.toString() + "/" + paymentRequest.ticket
                val result = payments.process(paymentRequest)

                ResponseEntity.created(URI(uri)).body(
                        RequestState(result.ticket, result.payment.boletoNumber, result.status)
                )

            }


    @ApiOperation(
            value = "Retrieves a payment request info according to the informed ticket",
            response = PaymentRequest::class,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            ApiResponse(code = 200, message = "Processed successfully"),
            ApiResponse(code = 400, message = "Bad Request", response = ApiExceptionMessage::class),
            ApiResponse(code = 404, message = "Payment not found", response = ApiExceptionMessage::class)
    )
    @GetMapping("/{ticket}")
    fun get(@PathVariable("ticket") ticket: String) =
            defer {
                payments.checkState(ticket)
            }

}

data class RequestState(
        val ticket: String,
        val boleto: String?,
        val status: ProcessState)

