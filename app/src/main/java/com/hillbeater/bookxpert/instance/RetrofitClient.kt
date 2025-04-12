package com.hillbeater.bookxpert.instance

import com.hillbeater.bookxpert.interfaces.PhoneDataApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: PhoneDataApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.restful-api.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhoneDataApiService::class.java)
    }
}