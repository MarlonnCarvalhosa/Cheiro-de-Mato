package com.marlonncarvalhosa.cheirodemato.util

import com.marlonncarvalhosa.cheirodemato.data.model.DateModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Calendar.toFormattedDate(): DateModel {
    val month_date = SimpleDateFormat("MMMM", Locale.getDefault())
    val year = this.get(Calendar.YEAR)
    var dayInt = this.get(Calendar.DAY_OF_MONTH)
    var dayConverted = "" + dayInt
    if (dayInt < 10) {
        dayConverted = "0$dayConverted"
    }

    val monthInt: Int = this.get(Calendar.MONTH) + 1
    var monthConverted = "" + monthInt
    if (monthInt < 10) {
        monthConverted = "0$monthConverted"
    }

    val month_name = month_date.format(this.time)

    return DateModel(dayConverted, monthConverted, year.toString(), month_name)
}