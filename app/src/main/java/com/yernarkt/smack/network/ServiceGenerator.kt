package com.yernarkt.smack.network

import com.yernarkt.smack.util.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceGenerator {
    private val HTTP_TIMEOUT: Long = 300000
    private var sServiceService: RetrofitService? = null
    private var retrofit: Retrofit? = null

    fun getRetrofitService(): RetrofitService? {
        if (sServiceService == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            sServiceService = retrofit!!.create<RetrofitService>(RetrofitService::class.java)
            return sServiceService
        } else {
            return sServiceService
        }
    }
}