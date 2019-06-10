package com.wirecard.payment.api.application

import com.wirecard.payment.api.domain.collectViolationsWithoutThrowing
import com.wirecard.payment.api.domain.exceptions.Violation
import com.wirecard.payment.api.domain.payment.Card
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@RestController
@RequestMapping("/validation")
class ValidationController{

    @PostMapping("/card")
    fun validateCard(@RequestBody card: Card):Mono<Map<String, Any>>{
        val violations = card.collectViolationsWithoutThrowing()

        return mapOf(
                "issuer" to card.getIssuer(),
                "valid" to violations.isEmpty().toString(),
                "violations" to violations.map(Violation::message)
        ).toMono()

    }


}