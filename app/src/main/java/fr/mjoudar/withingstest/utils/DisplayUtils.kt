package fr.mjoudar.withingstest.utils

import android.content.Context


/**
 * Return the number of columns that can fit in a Grid depending on the screen dimensions
 */
fun Context.columnNumberCalculator() : Int {
    val recyclerViewItemWidth = 150
    val displayMetrics = this.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth / recyclerViewItemWidth).toInt()
}