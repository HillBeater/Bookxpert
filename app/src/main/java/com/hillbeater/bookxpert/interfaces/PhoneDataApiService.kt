package com.hillbeater.bookxpert.interfaces

import com.hillbeater.bookxpert.model.PhoneDataAPIModel
import retrofit2.http.GET

interface PhoneDataApiService {
    @GET("objects")
    suspend fun getPhones(): List<PhoneDataAPIModel>
}