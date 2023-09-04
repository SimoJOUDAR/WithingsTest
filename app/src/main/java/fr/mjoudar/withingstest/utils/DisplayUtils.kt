package fr.mjoudar.withingstest.utils

import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager


/**
 * Return the number of columns that can fit in a Grid depending on the screen dimensions
 */
fun Context.columnNumberCalculator() : Int {
    val recyclerViewItemWidth = 150
    val displayMetrics = this.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth / recyclerViewItemWidth).toInt()
}

fun Context.hideSoftInput( iBinder: IBinder) {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(iBinder, 0)
}