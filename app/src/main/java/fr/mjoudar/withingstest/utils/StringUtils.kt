package fr.mjoudar.withingstest.utils

import java.text.SimpleDateFormat
import java.util.*


fun getGifFileNaming(): String {
    val currentDateAndTime = Date()
    val dateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    return "output_" + dateFormat.format(currentDateAndTime) + ".gif"

}