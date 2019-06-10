package com.wirecard.payment.api.domain.exceptions

class ValidationException(val violations:List<Violation>) :
        RuntimeException("Validation failures occurred: \n ${violations.joinToString("\n") { it.message }}")