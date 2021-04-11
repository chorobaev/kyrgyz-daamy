package io.flaterlab.kyrgyzdaamy.extension

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snackbar(text: String = "") {

    Snackbar.make(view!!, text, Snackbar.LENGTH_LONG).show()
}