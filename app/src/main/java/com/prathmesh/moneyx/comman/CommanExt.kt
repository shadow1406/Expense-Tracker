package com.prathmesh.moneyx.comman

import java.text.SimpleDateFormat
import java.util.*

fun String.toDateInMillis() : Long {
    val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
    val date = sdf.parse(this)
    return date?.time ?: System.currentTimeMillis()
}

fun String.dateToMillis() : Long {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val date = sdf.parse(this)
    return date?.time ?: System.currentTimeMillis()
}

fun String.monthToMillis(): Long {
    val sdf = SimpleDateFormat("MM-yyyy", Locale.getDefault())
    val date = sdf.parse(this)
    return date?.time ?: System.currentTimeMillis()
}

fun String.getMonthInWords() : String{
    return when(this){
        "1" -> "January"
        "2" -> "February"
        "3" -> "March"
        "4" -> "April"
        "5" -> "May"
        "6" -> "June"
        "7" -> "July"
        "8" -> "August"
        "9" -> "September"
        "10" -> "October"
        "11" -> "November"
        else -> "December"
    }
}

fun CharSequence.toMonthInt() : String{
    return when(this){
        "January" -> "1"
        "February" -> "2"
        "March" -> "3"
        "April" -> "4"
        "May" -> "5"
        "June" -> "6"
        "July" -> "7"
        "August" -> "8"
        "September" -> "9"
        "October" -> "10"
        "November" -> "11"
        else -> "12"
    }
}