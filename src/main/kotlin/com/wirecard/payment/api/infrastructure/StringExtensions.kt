package com.wirecard.payment.api.infrastructure

fun String?.hasCPFFormat() = this?.matches("([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-?[0-9]{2})".toRegex()) ?: false

fun String?.isEmail() = this?.matches("[0-9a-zA-Z-_.]+@[0-9a-zA-Z-]+([.][0-9a-zA-Z]+)+".toRegex()) ?: false

fun String?.isNumeric() = this?.matches("^[0-9]+$".toRegex()) ?: false

fun String?.hasMaxLengthOf(size: Int) = (this?.length ?: 0) <= size

fun String?.hasMinLengthOf(size: Int) = (this?.length ?: 0) >= size

fun String?.hasLengthBetween(min: Int, max: Int) = this.hasMinLengthOf(min) && this.hasMaxLengthOf(max)

fun String?.startsWithOneOf(vararg values: String) =
        !values.find { this?.startsWith(it) ?: false }.isNullOrBlank()

fun String?.startsWithOneOf(range: IntRange): Boolean {
    if(this.isNullOrEmpty()) return false

    for (i in range) {
        if (this!!.startsWith(i.toString())) return true
    }

    return false

}
