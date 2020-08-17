package com.intelegain.agora.api.urls

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by suraj.m on 9/8/17.
 */
object RetrofitClient {
    @JvmField
    var retrofit: Retrofit? = null
    /**
     * Initialize retrofit interceptor,http client and retrofit builder.
     * create an instance using the Retrofit Builder class and configure it with a base URL.
     *
     * @param API_BASE_URL - base url
     * @return retrofit instance
     */
    @JvmStatic
    fun getInstance(API_BASE_URL: String?): Retrofit? {
        if (retrofit == null) {
            val gson = GsonBuilder()
                    .setLenient()
                    .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
                    .readTimeout(30000, TimeUnit.MILLISECONDS)
                    .addInterceptor(interceptor)
                    .connectTimeout(50000, TimeUnit.MILLISECONDS)
                    .writeTimeout(30000, TimeUnit.MILLISECONDS)
                    .build()

            retrofit = Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build()

        }
        return retrofit
    }
}