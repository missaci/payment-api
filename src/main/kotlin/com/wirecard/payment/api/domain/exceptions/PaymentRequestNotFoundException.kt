package com.wirecard.payment.api.domain.exceptions

class PaymentRequestNotFoundException(val ticket: String) :
        RuntimeException("Payment request of ticket $ticket not found.")