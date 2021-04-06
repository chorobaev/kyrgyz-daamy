package com.timelysoft.amore.extension

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun String.convertToMin(date: DateFromTo): Int {

    return if (date == DateFromTo.DateTo) {
        if (substringBefore(":").toInt() == 0) {
            24 * 60 + substringAfter(":").toInt()
        } else {
            substringBefore(":").toInt() * 60 + substringAfter(":").toInt()
        }
    } else {
        substringBefore(":").toInt() * 60 + substringAfter(":").toInt()

    }

}

//17:45
fun String.checkInBetween2(dateFrom: String, dateTo: String): Boolean {
    val time1 = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(dateFrom)
    val calendar1 = Calendar.getInstance()
    time1?.let {
        calendar1.time = time1
        calendar1.add(Calendar.DATE, 1)
    }


    val time2 = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(dateTo)
    /*
    val calendar2 = Calendar.getInstance()
    time2?.let {
        calendar2.time = it
        calendar2.add(Calendar.DATE, 1)
    }

     */

    val d = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(this)
    val calendar3 = Calendar.getInstance()
    /*
    d?.let {
        calendar3.time = d
        calendar3.add(Calendar.DATE, 1)
    }
    val x = calendar3.time

     */

    return d in time1..time2//x.after(calendar1.time) && x.before(calendar2.time)
}


fun String.checkInBetween(dateFrom: String, dateTo: String): Boolean {
    val minFrom = dateFrom.convertToMin(DateFromTo.DateFrom)
    val minTo = dateTo.convertToMin(DateFromTo.DateTo)
    val current = this.convertToMin(DateFromTo.DateFrom)
    if (current in minFrom..minTo) {
        return true
    }
    return false
}

enum class DateFromTo {
    DateFrom,
    DateTo
}

fun Int.toHour(): String {
    val hour = this / 60
    val minutes = this % 60
    return "$hour:$minutes"
}

fun String.toDate(
    dateFormat: String = "HH:mm",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date? {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.parse(this)
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}


fun String.toUtc(): String {
    return try {
        val time = this.replace("T", " ").substring(0, 16)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val date: Date = format.parse(time)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val timezone = SimpleDateFormat("Z").format(calendar.time)
        val operation = timezone.substring(0, 1)
        val hour = timezone.substring(1, 3).toInt()
        val minute = timezone.substring(3, 5).toInt()
        if (operation == "-") {
            calendar.add(Calendar.HOUR, hour)
            calendar.add(Calendar.MINUTE, minute)
        } else if (operation == "+") {
            calendar.add(Calendar.HOUR, hour * -1)
            calendar.add(Calendar.MINUTE, minute * -1)
        }
        format.format(calendar.time).replace(" ", "T")
    } catch (e: Exception) {
        this
    }
}


fun String.toServerDate(time: String = "00:00"): String {
    return (this.substring(6) + "-" + this.substring(3, 5) + "-" + this.substring(
        0,
        2
    ) + "T${time}").toUtc() + ":00+00:00"
}
