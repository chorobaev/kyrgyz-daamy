package com.timelysoft.amore.cachingRetrofit

import android.util.Log
import com.timelysoft.amore.App
import kotlinx.io.IOException
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import java.util.concurrent.TimeUnit

class OfflineCacheInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val invocation : Invocation? = request.tag(Invocation::class.java)

        if (invocation != null){
             val annotation = invocation.method().getAnnotation(Cacheable::class.java)
            if (annotation != null && annotation.annotationClass.simpleName.equals("Cacheable") && !App.instance!!.isNetworkConnected()){
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()

                Log.d("CacheInterceptor : ", "was called")
                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            }
            else{
                Log.d("CacheInterceptor : ", "was not called")
            }
        }
        return chain.proceed(request)

    }
}