package com.wirecard.payment.api.infrastructure.repositories.h2

import com.wirecard.payment.api.domain.PaymentRequestRepository
import com.wirecard.payment.api.domain.payment.PaymentRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class H2PaymentRequestRepository
    @Autowired constructor(private val jdbc:JdbcTemplate):PaymentRequestRepository {


    override fun save(request: PaymentRequest): PaymentRequest {
        var insert = "insert into payments (" +
                "ticket, client_id, status, buyer_name, buyer_email, " +
                "buyer_cpf, amount, type, boleto_num, card_holder, card_number, " +
                "card_expiration) " +
                "values " +
                "(?, ?, ?, ?, ?," +
                "?, ?, ?, ?, ?, ?" +
                "?)"

        request.apply {
            jdbc.update(insert,
                    ticket,
                    clientId,
                    status.ordinal,
                    buyer.name,
                    buyer.email,
                    buyer.cpf.value,
                    payment.amount,
                    payment.type.ordinal,
                    payment.boletoNumber,
                    payment.card?.holderName,
                    payment.card?.number,
                    payment.card?.expirationDate?.toDate()
            )
        }

        return request

    }

}