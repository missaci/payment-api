package com.wirecard.payment.api.application

import com.wirecard.payment.api.domain.exceptions.Violation
import com.wirecard.payment.api.domain.payment.Card
import com.wirecard.payment.api.domain.payment.collectViolationsWithoutThrowing
import com.wirecard.payment.api.infrastructure.concurrency.defer
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/validation")
class ValidationController {

    @ApiOperation(
            value = "Validates a given card and informs its issuer",
            response = ValidationInfo::class,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            ApiResponse(code = 200, message = "Valid card"),
            ApiResponse(code = 412, message = "Invalid card", response = ValidationInfo::class)
    )
    @PostMapping("/card")
    fun validateCard(@RequestBody card: Card) =
            defer {
                val violations = card.collectViolationsWithoutThrowing()

                val result = ValidationInfo(
                        issuer = card.identifyIssuer(),
                        valid = violations.isEmpty(),
                        violations = violations.map(Violation::message)
                )

                result.toResponseEntity()

            }

}

data class ValidationInfo(val issuer: String, val valid: Boolean, val violations: List<String>?) {

    fun toResponseEntity() = when (valid) {
        true -> ResponseEntity.ok().body(this)
        else -> ResponseEntity.status(412).body(this)
    }
}