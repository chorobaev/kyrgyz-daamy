package com.timelysoft.amore.extension

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun String.timeToLong(): Long {
    return try {
        val time = this.replace("T", " ").substring(0, 16)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        format.parse(time).time
    } catch (e: Exception) {
        0L
    }
}



fun String.convertToMin(date: DateFromTo):Int{

    return if (date == DateFromTo.DateTo){
        if (substringBefore(":").toInt() == 0) {
            24*60 + substringAfter(":").toInt()
        }else{
            substringBefore(":").toInt()*60 + substringAfter(":").toInt()
        }
    }else{
        substringBefore(":").toInt()*60 + substringAfter(":").toInt()

    }

}

//17:45
fun String.checkInBetween2(dateFrom: String, dateTo: String):Boolean{
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



fun String.checkInBetween(dateFrom: String, dateTo: String):Boolean{
    val minFrom = dateFrom.convertToMin(DateFromTo.DateFrom)
    val minTo = dateTo.convertToMin(DateFromTo.DateTo)
    val current = this.convertToMin(DateFromTo.DateFrom)
    if (current in minFrom..minTo){
        return true
    }
    return false
}

enum class DateFromTo{
    DateFrom,
    DateTo
}
fun Int.toHour():String{
    val hour = this /60
    val minutes = this%60
    return "$hour:$minutes"
}

fun String.toDate(dateFormat: String = "HH:mm", timeZone: TimeZone = TimeZone.getTimeZone("UTC")):Date? {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.parse(this)
}
fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}

fun String.toMyTimezone(): String {
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
        if (operation == "+") {
            calendar.add(Calendar.HOUR, hour)
            calendar.add(Calendar.MINUTE, minute)
        } else if (operation == "-") {
            calendar.add(Calendar.HOUR, hour * -1)
            calendar.add(Calendar.MINUTE, minute * -1)
        }
        format.format(calendar.time)
    } catch (e: Exception) {
        this
    }
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

fun String.toMyDate(): String {
    return try {
        this.substring(8, 10) + "." + this.substring(5, 7) + "." + this.substring(0, 4)
    } catch (e: java.lang.Exception) {
        this
    }
}

fun String.toMyDateTime(): String {
    return try {
        this.substring(8, 10) + "." + this.substring(5, 7) + "." + this.substring(
            0,
            4
        ) + " " + this.substring(11, 16)
    } catch (e: java.lang.Exception) {
        this
    }
}


fun String.toServerDate(time: String = "00:00"): String {
    return (this.substring(6) + "-" + this.substring(3, 5) + "-" + this.substring(
        0,
        2
    ) + "T${time}").toUtc() + ":00+00:00"
}

fun String.isValidEmail(): Boolean {
    return try {
        val regExpn =
            ("""^(([\w-]+\.)+[\w-]+|([a-zA-Z]{1}|[\w-]{2,}))@((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|([a-zA-Z]+[\w-]+\.)+[a-zA-Z]{2,4})$""")
        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(this)
        matcher.matches()
    } catch (e: Exception) {
        false
    }

}

fun String.isValidPhone(): Boolean {
    return this.replace(" ", "").length == 9
}

fun String.toFullPhone(): String {
    return "996" + this.replace(" ", "")
}


