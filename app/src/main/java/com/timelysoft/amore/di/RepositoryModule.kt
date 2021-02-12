package com.timelysoft.amore.di

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.timelysoft.amore.App
import com.timelysoft.amore.cachingRetrofit.NetworkInterceptor
import com.timelysoft.amore.cachingRetrofit.OfflineCacheInterceptor
import com.timelysoft.amore.service.ApiServiceMod
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.NetworkRepositoryMod
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val module = module {
    single {NetworkRepositoryMod(get(), Dispatchers.IO)}
    single { loggingInterceptor() }
    single { cache() }
    single { OfflineCacheInterceptor() }
    single { NetworkInterceptor() }
    single { authInterceptor() }
    single { apiServiceMod(get()) }
    single { provideHttpClient(get(), get(),get(),get(),get())}
}

fun apiServiceMod(okHttpClient: OkHttpClient): ApiServiceMod {

    val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("${AppPreferences.baseUrl}api/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
    return retrofit.create(ApiServiceMod::class.java)
}


private fun provideHttpClient(authInterceptor: Interceptor, loggingInterceptor: HttpLoggingInterceptor,
                              networkInterceptor: NetworkInterceptor, offlineCacheInterceptor: OfflineCacheInterceptor, cache: Cache
): OkHttpClient {
    return OkHttpClient().newBuilder()
        .cache(cache)
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(networkInterceptor)
        .addInterceptor(offlineCacheInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()
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

private fun cache(): Cache? {
    val cacheSize = 5 * 1024 * 1024.toLong()
    return Cache(App.instance!!.applicationContext.cacheDir, cacheSize)
}

fun authInterceptor() :Interceptor {
    return Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ")
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }
}
