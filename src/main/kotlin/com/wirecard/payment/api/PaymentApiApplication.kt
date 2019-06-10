package com.wirecard.payment.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PaymentApiApplication

fun main(args: Array<String>) {
	runApplication<PaymentApiApplication>(*args)
}
