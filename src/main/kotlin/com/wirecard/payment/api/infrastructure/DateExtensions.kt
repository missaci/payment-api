package com.wirecard.payment.api.infrastructure

import java.text.SimpleDateFormat
import java.util.*

fun Date?.format(format:String) = this?.let { SimpleDateFormat(format).format(this) }