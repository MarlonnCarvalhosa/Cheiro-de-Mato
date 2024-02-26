package com.marlonncarvalhosa.cheirodemato.util

import android.annotation.SuppressLint
import com.marlonncarvalhosa.cheirodemato.util.Validation.isEmailValid
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun String.isValidEmail(): Boolean {
    return isEmailValid(this)
}

fun String.isStrongPassword(): Boolean {
    return (this.length >= 6)
}

fun String.isValidZipCode(): Boolean {
    return (this.length == 9)
}

fun String.isValidFullDate(): Boolean {
    return (this.length == 10)
}

fun String.isValidPhone(): Boolean {
    return (this.length == 15)
}


fun String.isValidCPF(): Boolean {
    return (this.length == 14)
}

fun String.isValidQuantity(): Boolean {
    return when {
        isNullOrEmpty() -> false
        this.toInt() <= 0 -> false
        this == "null" -> false
        else -> true
    }

}

fun Double.formatAsCurrency(): String {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return currencyFormat.format(this)
}

@SuppressLint("SimpleDateFormat")
fun String.convertDateFormat(initDateFormat: String, endDateFormat: String): String {
    return try {
        val sdf = SimpleDateFormat(initDateFormat)
        val output = SimpleDateFormat(endDateFormat)
        val d: Date = sdf.parse(this)
        output.format(d)

    } catch (e: ParseException) {
        e.printStackTrace()
        "Erro ao obter data"
    }
}

fun String.toDate(): LocalDate? {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return LocalDate.parse(this, formatter)
}


fun String.clearToLong(): Long {
    return this
        .replace("[^0-9]".toRegex(), "")
        .toLong()
}