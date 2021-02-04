package com.timelysoft.kainarapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.timelysoft.kainarapp.di.module
import com.timelysoft.kainarapp.di.viewModelModule
import com.timelysoft.kainarapp.service.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

class App : Application() {



    companion object {
        var instance: App? = null

    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(getModule())
        }
    }

    private fun getModule(): List<Module> {
        return listOf(viewModelModule, module)
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