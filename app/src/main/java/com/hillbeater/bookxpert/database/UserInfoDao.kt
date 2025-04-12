package com.hillbeater.bookxpert.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userInfo: UserInfo)

    @Query("SELECT * FROM user_info LIMIT 1")
    suspend fun getUserInfo(): UserInfo?

    @Query("DELETE FROM user_info")
    suspend fun clearAllData()

    @Query("UPDATE user_info SET fcmToken = :token")
    suspend fun updateFcmToken(token: String?)

}