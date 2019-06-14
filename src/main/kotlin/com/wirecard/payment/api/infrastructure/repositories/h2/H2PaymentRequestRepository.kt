package com.wirecard.payment.api.infrastructure.repositories.h2

import com.wirecard.payment.api.domain.payment.PaymentRequestRepository
import com.wirecard.payment.api.domain.payment.*
import com.wirecard.payment.api.infrastructure.format
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class H2PaymentRequestRepository
@Autowired constructor(private val jdbc: JdbcTemplate) : PaymentRequestRepository {


    override fun find(ticket: String): PaymentRequest? {
        return try {
            jdbc.queryForObject("select " +
                    "ticket, client_id, status, " +
                    "buyer_name, buyer_email, " +
                    "buyer_cpf, amount, type, " +
                    "boleto_num, card_holder, card_number, " +
                    "card_expiration from payments " +
                    "where ticket = ?",
                    arrayOf(ticket),
                    this.mapper())

        } catch (empty: EmptyResultDataAccessException) {
            null
        }
    }


    override fun save(request: PaymentRequest): PaymentRequest {
        var insert = "insert into payments (" +
                "ticket, client_id, status, buyer_name, buyer_email, " +
                "buyer_cpf, amount, type, boleto_num, card_holder, card_number, " +
                "card_expiration) " +
                "values " +
                "(?, ?, ?, ?, ?," +
                "?, ?, ?, ?, ?, ?, " +
                "?)"

        request.apply {
            jdbc.update(insert,
                    ticket,
                    clientId,
                    status.ordinal,
                    buyer.name,
                    buyer.email,
                    buyer.cpf.toOnlyDigits(),
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

    private fun mapper() = RowMapper { rs: ResultSet, _: Int ->
        val paymentType = PaymentType.values()[rs.getInt("type")]

        val card = getCardData(paymentType, rs)

        PaymentRequest(
                clientId = rs.getLong("client_id"),
                ticket = rs.getString("ticket"),
                status = ProcessState.values()[rs.getInt("status")],
                buyer = Buyer(
                        name = rs.getString("buyer_name"),
                        email = rs.getString("buyer_email"),
                        cpf = rs.getString("buyer_cpf")
                ),
                payment = Payment(
                        amount = rs.getDouble("amount"),
                        type = paymentType,
                        boletoNumber = rs.getString("boleto_num"),
                        card = card
                )
        )

    }

    private fun getCardData(paymentType: PaymentType, rs: ResultSet) =
            if (PaymentType.CARD == paymentType) {
                Card(
                        holderName = rs.getString("card_holder"),
                        number = rs.getString("card_number"),
                        expirationDate = rs.getDate("card_expiration").format("MM/yyyy")!!
                )

            } else {
                null
            }

}