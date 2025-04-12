package com.hillbeater.bookxpert.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PhoneDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PhoneDataItem>)

    @Update
    suspend fun updateItem(item: PhoneDataItem)

    @Delete
    suspend fun deleteItem(item: PhoneDataItem)

    @Query("SELECT * FROM phone_data")
    suspend fun getAllItems(): List<PhoneDataItem>

    @Query("DELETE FROM phone_data")
    fun deleteAll()
}