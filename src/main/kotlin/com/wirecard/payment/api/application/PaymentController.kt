package com.wirecard.payment.api.application

import com.wirecard.payment.api.domain.Payments
import com.wirecard.payment.api.domain.payment.PaymentRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.toMono

@RestController
@RequestMapping("/payments")
class PaymentController
@Autowired constructor(private val payments: Payments) {

    @PostMapping
    fun create(request: PaymentRequest) =
            payments.process(request).toMono()

}