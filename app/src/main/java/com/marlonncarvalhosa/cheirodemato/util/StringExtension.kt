package com.marlonncarvalhosa.cheirodemato.util

import android.annotation.SuppressLint
import android.graphics.Color
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.util.Validation.isEmailValid
import java.text.Normalizer
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

fun String.removeAccents(): String {
    val normalizedString = Normalizer.normalize(this, Normalizer.Form.NFD)
    return Regex("[^\\p{ASCII}]").replace(normalizedString, "")
}

fun Int.toKilograms(): Double {
    return try {
        val grams = this
        val kilograms = grams / 1000.0
        kilograms
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        0.0
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

fun calculatePercentageStock(amount: Int?, amountInitStock: Int?): Int {
    val percentage = (amountInitStock?.toDouble()?.let { amount?.toDouble()?.div(it) })?.times(100)

    return when {
        percentage != null -> when {
            percentage >= 50.0 -> R.color.status_green
            percentage >= 25.0 -> R.color.status_yellow
            percentage >= 10.0 -> R.color.status_orange
            percentage >= 5.0 -> R.color.status_red
            else -> R.color.status_red
        }
        else -> Color.WHITE
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