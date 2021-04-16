package io.flaterlab.kyrgyzdaamy.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.flaterlab.kyrgyzdaamy.App
import io.flaterlab.kyrgyzdaamy.cachingRetrofit.Cacheable
import io.flaterlab.kyrgyzdaamy.service.ApiServiceMod
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {


    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiServiceMod =
        Retrofit.Builder().baseUrl("${AppPreferences.baseUrl}api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(ApiServiceMod::class.java)


    @AuthInterceptorOkHttp
    @Singleton
    @Provides
    fun authInterceptor(): Interceptor {
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

    @NetworkInterceptorOkHttp
    @Singleton
    @Provides
    fun networkInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            val cacheControl = CacheControl.Builder().maxAge(5, TimeUnit.MINUTES)
                .build()

            request = request.newBuilder().removeHeader(AppPreferences.HEADER_CACHE_CONTROL)
                .cacheControl(cacheControl).build()

            Log.d("CacheInterceptor : ", "was called network")
            chain.proceed(request)
        }
    }

    @OfflineInterceptorOkHttp
    @Singleton
    @Provides
    fun offlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            val invocation: Invocation? = request.tag(Invocation::class.java)

            if (invocation != null) {
                val annotation = invocation.method().getAnnotation(Cacheable::class.java)
                if (annotation != null && annotation.annotationClass.simpleName.equals("Cacheable") && !App.instance!!.isNetworkConnected()) {
                    val cacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()

                    Log.d("CacheInterceptor : ", "was called")
                    request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build()
                } else {

                    Log.d("CacheInterceptor : ", "was not called")

                }
            }
            chain.proceed(request)

        }
    }

    @Singleton
    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("OkHttp", message)
            }

        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    @Singleton
    @Provides
    fun provideFireStoreInstance():FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFireStorageInstance():FirebaseStorage = FirebaseStorage.getInstance()



    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NetworkInterceptorOkHttp

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OfflineInterceptorOkHttp


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorOkHttp

    @Singleton
    @Provides
    fun provideOkHttp(
        @AuthInterceptorOkHttp authInterceptor: Interceptor, cache: Cache,
        loggingInterceptor: HttpLoggingInterceptor,
        @NetworkInterceptorOkHttp networkInterceptor: Interceptor,
        @OfflineInterceptorOkHttp offlineCacheInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .cache(cache)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addInterceptor(offlineCacheInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun cache(): Cache {
        val cacheSize = 5 * 1024 * 1024.toLong()
        return Cache(
            App.instance!!.applicationContext.cacheDir,
            cacheSize
        )
    }

    @Singleton
    @Provides
    fun provideNetworkInfo(@ApplicationContext context: Context): NetworkInfo? {
        val cM = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cM.activeNetworkInfo
    }


}
