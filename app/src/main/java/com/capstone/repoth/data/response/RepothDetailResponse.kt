package com.capstone.repoth.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "report")
data class RepothDetailResponse(
    @PrimaryKey @field:SerializedName("id") val id: String,
    val createdAt: String,
    val description: String,
    val lat: Double,
    val lon: Double,
    val photoUrl: String
)