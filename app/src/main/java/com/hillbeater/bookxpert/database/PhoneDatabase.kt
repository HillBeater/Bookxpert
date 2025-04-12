package com.hillbeater.bookxpert.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [PhoneDataItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class PhoneDatabase : RoomDatabase() {
    abstract fun phoneDao(): PhoneDataDao

    companion object {
        @Volatile private var INSTANCE: PhoneDatabase? = null

        fun getDatabase(context: Context): PhoneDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    PhoneDatabase::class.java,
                    "phone_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}