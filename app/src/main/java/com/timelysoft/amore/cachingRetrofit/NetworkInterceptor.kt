package com.timelysoft.amore.cachingRetrofit

import android.util.Log
import com.timelysoft.amore.service.AppPreferences
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val cacheControl = CacheControl.Builder().maxAge(5, TimeUnit.MINUTES)
            .build()

        request = request.newBuilder().removeHeader(AppPreferences.HEADER_CACHE_CONTROL).cacheControl(cacheControl).build()

        Log.d("CacheInterceptor : ", "was called network")



        return chain.proceed(request)
    }

}