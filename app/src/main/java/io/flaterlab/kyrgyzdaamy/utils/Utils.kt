package io.flaterlab.kyrgyzdaamy.utils

object Utils {
    fun convertDate(day: Int, month: Int, year: Int): String {
        var date = ""

        date += if (day < 10) {
            "0$day"
        } else {
            day.toString()
        }
        date += "."


        date += if (month < 10) {
            "0$month"

        } else {
            month.toString()
        }
        date += ".$year"
        return date
    }
}