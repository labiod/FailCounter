package com.kgb.failcounter

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        fun getDateInFormat(date: Date, format: String): String {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            return dateFormat.format(date)
        }
    }
}