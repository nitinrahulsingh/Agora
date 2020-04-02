package com.intelegain.agora.api.urls

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            retrofit = Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            //            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS)
//                    .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY)).build();
//            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(ScalarsConverterFactory.create());
//            retrofit = builder.client(httpClient).build();
        }
        return retrofit
    }
}