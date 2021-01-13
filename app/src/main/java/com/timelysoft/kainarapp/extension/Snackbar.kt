package com.timelysoft.kainarapp.extension

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snackbar(text: String = "") {
    try {
        Snackbar.make(view!!, text, Snackbar.LENGTH_LONG).show()
    } catch (e: Exception) {
    }
}