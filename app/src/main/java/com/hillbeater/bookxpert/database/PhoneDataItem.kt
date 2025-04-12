package com.hillbeater.bookxpert.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phone_data")
data class PhoneDataItem(
    @PrimaryKey
    val id: String,
    val name: String,
    val color: String? = null,
    val capacity: String? = null,
    val price: String? = null,
    val generation: String? = null,
    val year: Int? = null,
    val cpuModel: String? = null,
    val hardDiskSize: String? = null,
    val strapColour: String? = null,
    val caseSize: String? = null,
    val screenSize: Double? = null,
    val description: String? = null
)
