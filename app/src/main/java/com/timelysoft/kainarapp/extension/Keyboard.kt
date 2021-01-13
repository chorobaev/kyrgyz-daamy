package com.timelysoft.kainarapp.extension

import android.view.View
import android.view.inputmethod.InputMethodManager


fun View.hideSoftKeyboard() {
    val inputMethodManager = context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
}