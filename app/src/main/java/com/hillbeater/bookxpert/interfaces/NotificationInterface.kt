package com.hillbeater.bookxpert.interfaces

import com.hillbeater.bookxpert.instance.AccessToken
import com.hillbeater.bookxpert.model.FCMRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationInterface {

    @POST("https://fcm.googleapis.com/v1/projects/authentications-ed72e/messages:send")
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    fun sendNotification(
        @Body messaging: FCMRequest,
        @Header("Authorization") accessToken: String = "Bearer ${AccessToken.getAccessToken()}"
    ): Call<ResponseBody>
}
