package com.timelysoft.kainarapp.extension

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.timelysoft.kainarapp.R

import kotlinx.coroutines.*

private lateinit var dialog: AlertDialog

fun Fragment.loadingShow() {
    try {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.alert_loading, null)
        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    } catch (e: Exception) {

    }
}

fun Fragment.successfullyAdded(){

}


fun Fragment.loadingHide(time: Long = 0) {
    try {
        CoroutineScope(Dispatchers.IO).launch {
            delay(time)
            withContext(Dispatchers.Main) {
                dialog.dismiss()
            }
        }
    } catch (e: Exception) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                dialog.dismiss()
            }
        }
    }
}


