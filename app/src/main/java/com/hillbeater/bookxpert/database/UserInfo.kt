package com.hillbeater.bookxpert.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userName: String,
    val userEmail: String,
    val userProfileUrl: String = "",
    val fcmToken: String? = null
)

