package com.wirecard.payment.api.application

import com.wirecard.payment.api.domain.Payments
import com.wirecard.payment.api.domain.payment.PaymentRequest
import com.wirecard.payment.api.domain.payment.ProcessState
import com.wirecard.payment.api.infrastructure.concurrency.defer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/payments")
class PaymentController
@Autowired constructor(private val payments: Payments) {

    @PostMapping
    fun create(@RequestBody request: PaymentRequest) =
            defer {
                val result = payments.process(request)
                RequestState(result.ticket, result.payment.boletoNumber, result.status)
            }


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

