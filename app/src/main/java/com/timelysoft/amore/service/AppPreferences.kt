package com.timelysoft.amore.service

import android.content.Context
import android.content.SharedPreferences


object AppPreferences {
    var change: Boolean = false




    const val baseUrlCRM = "https://kaynar-test.timelysoft.org:8041/"
    const val baseUrl = "https://deliverycarbis-test.timelysoft.org:5096/"
    //const val baseUrl = "https://saturn.carbis.ru:5849/"
    const val HEADER_CACHE_CONTROL = "Cache-Control"

    fun group() = "C6CA8037-3667-400B-80C8-08D8C13995D1"
    //fun group() = "4ba28cd5-43a5-41a1-f3b2-08d8cf0dfc1c"

    private const val NAME = "Amore"
    private const val MODE = Context.MODE_PRIVATE
    lateinit var preferences: SharedPreferences


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var lastDay : String?
        set(value)  = preferences.edit {
            it.putString("lastDay", value)
        }
        get() = preferences.getString("lastDay", "")

    var currencyName : String?
        set(value) = preferences.edit{
            it.putString("currencyName",value)
        }
        get() = preferences.getString("currencyName","")


    var categoryId: String?
        set(value) = preferences.edit {
            it.putString("categoryId", value)
        }
        get() = preferences.getString("categoryId", "")


    var bankPay: Boolean
        get() = preferences.getBoolean("bankPay", false)
        set(value) = preferences.edit() {
            it.putBoolean("bankPay", value)
        }

    var dateFrom:String?
        get() = preferences.getString("dateFrom","")
        set(value) =  preferences.edit {
            it.putString("dateFrom", value)
        }
    var dateTo:String?
        get() = preferences.getString("dateTo","")
        set(value) =  preferences.edit {
            it.putString("dateTo", value)
        }


    var amount: Int
        get() = preferences.getInt("amount", 0)
        set(value) = preferences.edit {
            it.putInt("amount", value)
        }

    fun clear() {
        isLogined = false
        accessToken = ""
        refreshToken = ""
        login = ""
        name = ""
        surname = ""
        phone = ""
    }

    var schedule:String?
    get() = preferences.getString("schedule", "")
    set(value) = preferences.edit {
        it.putString("schedule", value)
    }


    var isLogined: Boolean
        get() = preferences.getBoolean("isLogined", false)
        set(value) = preferences.edit {
            it.putBoolean("isLogined", value)
        }

    var accessToken: String?
        get() = preferences.getString("accessToken", "")
        set(value) = preferences.edit {
            it.putString("accessToken", value)
        }

    var login: String?
        get() = preferences.getString("login", "")
        set(value) = preferences.edit {
            it.putString("login", value)
        }

    var started: Boolean
        get() = preferences.getBoolean("started", false)
        set(value) = preferences.edit() {
            it.putBoolean("started", value)
        }

    var refreshToken: String?
        get() = preferences.getString("refreshToken", "")
        set(value) = preferences.edit {
            it.putString("refreshToken", value)
        }

    var language: String?
        get() = preferences.getString("language", "ru")
        set(value) = preferences.edit {
            it.putString("language", value)
        }


    var restaurant: String
        get() = preferences.getString("restaurant", "")!!
        set(value) = preferences.edit {
            it.putString("restaurant", value)
        }

    var restaurantPhoto: String
        get() = preferences.getString("restaurantPhoto", "")!!
        set(value) = preferences.edit {
            it.putString("restaurantPhoto", value)
        }

    var restaurantLogo: String
        get() = preferences.getString("restaurantPhotoLogo", "")!!
        set(value) = preferences.edit {
            it.putString("restaurantPhotoLogo", value)
        }

    var restaurantCRM: Int
        get() = preferences.getInt("restaurantCRM", -1)
        set(value) = preferences.edit {
            it.putInt("restaurantCRM", value)
        }

    var globalId: String
        get() {
            val data = preferences.getString("globalId", "")
            return if (data != null) return data else ""

        }
        set(value) = preferences.edit {
            it.putString("globalId", value)
        }

    var name: String
        get() {
            val data = preferences.getString("name", "")
            return if (data != null) return data else ""

        }
        set(value) = preferences.edit {
            it.putString("name", value)
        }

    var surname: String
        get() {
            val data = preferences.getString("Surnames", "")
            return if (data != null) return data else ""

        }
        set(value) = preferences.edit {
            it.putString("Surnames", value)
        }

    var phone: String
        get() {
            val data = preferences.getString("phone", "")
            return if (data != null) return data else ""

        }
        set(value) = preferences.edit {
            it.putString("phone", value)
        }

    var lastOrderType: Int
        get() = preferences.getInt("lastOrderType", -1)
        set(value) = preferences.edit {
            it.putInt("lastOrderType", value)
        }


    var lastOrderId: String
        get() {
            val data = preferences.getString("lastOrderId", "")
            return if (data != null) return data else ""

        }
        set(value) = preferences.edit {
            it.putString("lastOrderId", value)
        }

    var lastOrderRestaurantId: String
        get() {
            val data = preferences.getString("lastOrderRestaurantId", "")
            return if (data != null) return data else ""

        }
        set(value) = preferences.edit {
            it.putString("lastOrderRestaurantId", value)
        }


    fun version(restaurantId: String, version: String) {
        preferences.edit {
            it.putString("restaurantId", version)

        }
    }


    fun version(): String? {
        return preferences.getString("restaurantId", "")
    }


}