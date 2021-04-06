package com.timelysoft.amore

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatDelegate
import com.timelysoft.amore.service.AppPreferences
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {



    companion object {
        var instance: App? = null

    }
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        instance = this
        /*
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(getModule())
        }

         */
    }

    override fun attachBaseContext(base: Context) {
        AppPreferences.init(base)
        AppPreferences.amount = 0
        super.attachBaseContext(base)
    }

    fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability =
            cm.getNetworkCapabilities(cm.activeNetwork)
        return capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}