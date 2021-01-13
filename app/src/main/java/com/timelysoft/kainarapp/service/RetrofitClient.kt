package com.timelysoft.kainarapp.service

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.timelysoft.kainarapp.App
import com.timelysoft.kainarapp.cachingRetrofit.NetworkInterceptor
import com.timelysoft.kainarapp.cachingRetrofit.OfflineCacheInterceptor
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer " + AppPreferences.accessToken)
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                Log.d("OkHttp", message)
            }

        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val networkInterceptor = NetworkInterceptor()
    private val offlineCacheInterceptor = OfflineCacheInterceptor()

    private val client =
        OkHttpClient().newBuilder()
            .cache(cache())
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor())
            .addNetworkInterceptor(networkInterceptor)
            .addInterceptor(offlineCacheInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

    fun retrofit(baseUrl: String = "${AppPreferences.baseUrl}api/") =
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    fun retrofitCRM(baseUrl: String = "${AppPreferences.baseUrlCRM}api/") =
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    fun apiServiceMod(): ApiServiceMod {
        return retrofit().create(ApiServiceMod::class.java)
    }

    fun apiServiceCRM(): ApiServiceCRM {
        return retrofitCRM().create(ApiServiceCRM::class.java)
    }

    private fun cache(): Cache? {
        val cacheSize = 5 * 1024 * 1024.toLong()
        return Cache(App.instance!!.applicationContext.cacheDir, cacheSize)
    }
}
