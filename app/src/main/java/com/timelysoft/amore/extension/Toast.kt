package com.timelysoft.amore.extension

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(text: String? = "") {
    try {
        Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
    }

}