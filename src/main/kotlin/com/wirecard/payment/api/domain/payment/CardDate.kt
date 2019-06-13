package com.wirecard.payment.api.domain.payment

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation
import java.util.*

@JsonSerialize(using = ToStringSerializer::class)
data class CardDate(val value: String) : Validatable {

    override fun validate() {
        validateAndParse()
    }

    override fun toString(): String {
        return value
    }

    fun toDate(): Date {
        return validateAndParse()
    }

    private fun parse(date: String): Date {
        val splitDate = date.split("/")
        val month = splitDate[0].toInt() - 1
        val year = splitDate[1].toInt()
        val calendar = Calendar.getInstance()

        calendar.apply {
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, if (year < 80) year + 2000 else year)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DATE))
        }

        return calendar.time
    }

    private fun validateAndParse(): Date {
        val violations = mutableListOf<Violation>()

        if (!value.matches(DATE_PATTERN.toRegex())) {
            violations.add(Violation("Date could not be parsed: $value"))
            throw ValidationException(violations)
        }

        val parsedDate = parse(this.value)

        if (parsedDate.compareTo(Date()) == -1) {
            violations.add(Violation("Card date expired: $value"))
            throw ValidationException(violations)
        }

        return parsedDate

    }

    companion object {
        private const val DATE_PATTERN = "([1-9]|0[1-9]|1[0-2])/([0-9]{2}|[0-9]{4})"
    }
}