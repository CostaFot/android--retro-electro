package com.feelsokman.retroelectro.repository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class Repository(
    baseUrl: String,
    isDebugEnabled: Boolean,
    apiKey: String
) {

    private val apiKeyHeader: String = "x-api-key"
    val retrofit: Retrofit

    init {

        /*adding a logging interceptor when debug is true.
        you can check how your API call is going in the LogCat */
        val loggingInterceptor = HttpLoggingInterceptor()
        if (isDebugEnabled) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        // here's how you can add your api key as a header
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(apiKeyHeader, apiKey)
                .build()
            chain.proceed(request)
        }.addInterceptor(loggingInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
