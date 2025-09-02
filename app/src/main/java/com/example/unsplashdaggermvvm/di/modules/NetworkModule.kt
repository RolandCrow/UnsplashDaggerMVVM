package com.example.unsplashdaggermvvm.di.modules

import android.app.Application
import com.example.unsplashdaggermvvm.api.UnsplashService
import com.example.unsplashdaggermvvm.utils.BASE_URL_UNSPLASH
import com.example.unsplashdaggermvvm.utils.unsplash_access_key
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.jvm.Throws

@Module
object NetworkModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun providesOkhttp(cache: Cache): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addNetworkInterceptor(cacheInterceptor)
            .cache(cache)
            .addInterceptor { chain: okhttp3.Interceptor.Chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Client-ID$unsplash_access_key")
                    .build()
                chain.proceed(newRequest)
            }
        builder.addInterceptor(loggingInterceptor)
        return builder.build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_UNSPLASH)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): UnsplashService =
        retrofit.create(UnsplashService::class.java)

    @Provides
    @Singleton
    fun provideCache(app: Application): Cache {
        return Cache(
            File(app.applicationContext.cacheDir, "unsplash_image_cache"),
            10 * 1024*1024
        )
    }

    private val cacheInterceptor = object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(30, TimeUnit.DAYS)
                .build()
            return response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
    }
}