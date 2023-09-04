package fr.mjoudar.withingstest.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


fun getGifFileNaming(): String {
    val currentDateAndTime = Date()
    val dateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val r = "output_" + dateFormat.format(currentDateAndTime) + ".git"
    Log.d("Panda_test", r)
    return r

}