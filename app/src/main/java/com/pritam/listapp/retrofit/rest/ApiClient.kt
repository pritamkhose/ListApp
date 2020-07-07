package com.pritam.listapp.retrofit.rest

import com.pritam.listapp.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    val instance: ApiInterface = Retrofit.Builder().run {
        baseUrl(Constants.BASE_URL)
        addConverterFactory(GsonConverterFactory.create())
        client(createRequestInterceptorClient())
        build()
        
    }.create(ApiInterface::class.java)

    private fun createRequestInterceptorClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

}

