package io.flaterlab.kyrgyzdaamy.service

import android.content.Context
import android.content.SharedPreferences


object AppPreferences {


    //const val baseUrl = "https://deliverycarbis-test.timelysoft.org:5096/"
    const val baseUrl = "https://saturn.carbis.ru:5849/"
    const val HEADER_CACHE_CONTROL = "Cache-Control"

    //fun group() = "C6CA8037-3667-400B-80C8-08D8C13995D1"
    fun group() = "4ba28cd5-43a5-41a1-f3b2-08d8cf0dfc1c"
    fun idOfRestaurant() = "d1946984-d536-4bf0-1fd2-08d8cf124852"
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

    var phoneNumber: String?
        set(value) = preferences.edit {
            it.putString("phoneNumber",value)
        }
        get() = preferences.getString("phoneNumber","")


    var lastDay: String?
        set(value) = preferences.edit {
            it.putString("lastDay", value)
        }
        get() = preferences.getString("lastDay", "")

    var currencyName: String?
        set(value) = preferences.edit {
            it.putString("currencyName", value)
        }
        get() = preferences.getString("currencyName", "")

    var bankPay: Boolean
        get() = preferences.getBoolean("bankPay", false)
        set(value) = preferences.edit() {
            it.putBoolean("bankPay", value)
        }

    var dateFrom: String?
        get() = preferences.getString("dateFrom", "")
        set(value) = preferences.edit {
            it.putString("dateFrom", value)
        }
    var dateTo: String?
        get() = preferences.getString("dateTo", "")
        set(value) = preferences.edit {
            it.putString("dateTo", value)
        }


    var amount: Int
        get() = preferences.getInt("amount", 0)
        set(value) = preferences.edit {
            it.putInt("amount", value)
        }

    var schedule: String?
        get() = preferences.getString("schedule", "")
        set(value) = preferences.edit {
            it.putString("schedule", value)
        }


    var restaurant: String
        get() = preferences.getString("restaurant", "")!!
        set(value) = preferences.edit {
            it.putString("restaurant", value)
        }


    var name: String
        get() {
            val data = preferences.getString("name", "")
            return if (data != null) return data else ""

        }
        set(value) = preferences.edit {
            it.putString("name", value)
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