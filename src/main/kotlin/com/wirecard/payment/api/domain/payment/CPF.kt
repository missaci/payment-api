package com.wirecard.payment.api.domain.payment

import com.wirecard.payment.api.domain.Validatable
import com.wirecard.payment.api.domain.exceptions.ValidationException
import com.wirecard.payment.api.domain.exceptions.Violation
import com.wirecard.payment.api.infrastructure.hasCPFFormat

class CPF(val value: String): Validatable {

    override fun validate() {
        if( !isValid(value) ) throw ValidationException(listOf(Violation("CPF informed is not valid: $value")))
    }

    private fun isValid(value: String): Boolean {
        val formattedValue = value.replace("[^0-9]".toRegex(), "")

        if(!value.hasCPFFormat() || areAllDigitsTheSame(formattedValue)) return false

        val dig10 = digitCalcFor(9, 10, formattedValue)
        val dig11 = digitCalcFor(10, 11, formattedValue)

        if( dig10 == formattedValue[9] && dig11 == formattedValue[10]) return true

        return false
    }

    private fun digitCalcFor(maxIndex:Int, baseMultiplier: Int, cpf:String):Char {
        var sum = 0
        var multiplier = baseMultiplier

        for(i in 0 until maxIndex){
            val num = Character.getNumericValue(cpf[i])
            sum += (num * multiplier)
            multiplier--

        }

        var result = 11 - (sum % 11)

        return if((result == 10) || (result == 11)) '0'
        else result.toString()[0]

    }

    private fun areAllDigitsTheSame(value: String) =
            listOf( "00000000000",
                    "11111111111",
                    "22222222222",
                    "33333333333",
                    "44444444444",
                    "55555555555",
                    "66666666666",
                    "77777777777",
                    "88888888888",
                    "99999999999"
            ).contains(value)

}