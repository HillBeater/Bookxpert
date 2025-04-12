package com.hillbeater.bookxpert.instance

import com.hillbeater.bookxpert.interfaces.NotificationInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NotificationAPI {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com/") // FCM base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val notificationService: NotificationInterface = retrofit.create(NotificationInterface::class.java)
}
