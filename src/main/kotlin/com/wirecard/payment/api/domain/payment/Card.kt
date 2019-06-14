package com.wirecard.payment.api.domain.payment

import com.fasterxml.jackson.annotation.JsonCreator
import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation
import com.wirecard.payment.api.infrastructure.hasLengthBetween
import com.wirecard.payment.api.infrastructure.isNaturalNumber
import com.wirecard.payment.api.infrastructure.startsWithOneOf

class Card
@JsonCreator constructor(
        val holderName: String,
        val number: String,
        expirationDate: String,
        val cvv: String? = null
) : Validatable {

    val expirationDate = CardDate(expirationDate)

    override fun validate() {
        val violations = mutableListOf<Violation>()

        violations.addAll(expirationDate.collectViolationsWithoutThrowing())
        validateInternalFields(violations)

        if (violations.isNotEmpty()) throw ValidationException(violations)
    }

    fun identifyIssuer(): String {
        val number = this.number.replace("[\\s\t-]".toRegex(), "")

        return when {
            number.startsWithOneOf("34", "37") -> "American Express"
            number.startsWith("31") -> "China T-Union"
            number.startsWith("62") -> "China UnionPay"
            number.startsWithOneOf("36", "38", "39", "3095")
                    || number.startsWithOneOf(300..305) -> "Diners Club International"
            number.startsWithOneOf(51..55)
                    || number.startsWithOneOf(2221..2720) -> "MasterCard"
            number.startsWithOneOf("6521", "6522") -> "RuPay"
            number.startsWithOneOf("6011", "64", "65")
                    || number.startsWithOneOf(622126..622925)
                    || number.startsWithOneOf(624000..626999)
                    || number.startsWithOneOf(628200..628899) -> "Discover Card"
            number.startsWith("60") -> "RuPay"
            number.startsWithOneOf(636..639) -> "InstaPayment"
            number.startsWith("357111") -> "LankaPay"
            number.startsWithOneOf(3528..3589) -> "JCB"
            number.startsWithOneOf("6759", "676770", "676774") -> "Maestro UK"
            number.startsWith("50") || number.startsWithOneOf(56..69) -> "Maestro"
            number.startsWithOneOf("5019", "4571") -> "Dankort"
            number.startsWithOneOf(2200..2204) -> "MIR"
            number.startsWithOneOf(6054740..6054744) -> "NPS Pridnestrovie"
            number.startsWithOneOf(979200..979289) -> "Troy"
            number.startsWith("4") -> "Visa"
            number.startsWith("1") -> "UATP"
            number.startsWithOneOf(506099..506198)
                    || number.startsWithOneOf(650002..650027) -> "Verve"
            else -> "Unknown"
        }
    }

    private fun validateInternalFields(violations: MutableList<Violation>) =
            violations.apply {
                if (holderName.isNullOrBlank() || !holderName.hasLengthBetween(3, 150))
                    add(Violation("Card holder name cannot be empty and must have length between 3 and 150 characters."))

                if (!isCardNumberValid())
                    add(Violation("Card number is not valid."))

                if (!cvv.hasLengthBetween(3, 4) || !cvv.isNaturalNumber())
                    add(Violation("Invalid cvv format."))
            }

    private fun isCardNumberValid(): Boolean {
        val numberToValidate = this.number.replace("[\\s\t-]".toRegex(), "")

        return numberToValidate.isNaturalNumber() &&
                numberToValidate.hasLengthBetween(12, 19) &&
                isValidOnLuhnFormula(numberToValidate)

    }

    private fun isValidOnLuhnFormula(numberToValidate: String) =
            numberToValidate.reversed()
                    .toCharArray()
                    .map(Character::getNumericValue)
                    .mapIndexed { index, i -> if (index % 2 != 0) i * 2 else i }
                    .map { if (it > 9) (it % 10) + 1 else it }
                    .sum().rem(10) == 0
}



