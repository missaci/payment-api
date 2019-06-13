package com.wirecard.payment.api.application

import com.wirecard.payment.api.domain.payment.collectViolationsWithoutThrowing
import com.wirecard.payment.api.domain.exceptions.Violation
import com.wirecard.payment.api.domain.payment.Card
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/validation")
class ValidationController {

    @PostMapping("/card")
    fun validateCard(@RequestBody card: Card): ResponseEntity<ValidationInfo> {
        val violations = card.collectViolationsWithoutThrowing()

        val result = ValidationInfo(
                issuer = card.getIssuer(),
                valid = violations.isEmpty(),
                violations = violations.map(Violation::message)
        )

        return result.toResponseEntity()

    }


}

data class ValidationInfo(val issuer: String, val valid: Boolean, val violations: List<String>?) {

    fun toResponseEntity() = when (valid) {
        true -> ResponseEntity.ok().body(this)
        else -> ResponseEntity.status(412).body(this)
    }
}