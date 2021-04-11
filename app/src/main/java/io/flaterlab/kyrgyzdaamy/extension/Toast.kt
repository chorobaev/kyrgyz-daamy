package io.flaterlab.kyrgyzdaamy.extension

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(text: String? = "") {
    Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
}

fun Fragment.isConnectedOrConnecting(): Boolean {
    val connMgr = this.requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connMgr.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}