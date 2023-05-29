package com.capstone.repoth.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "repoth")
data class ListRepoth(
    @PrimaryKey
    val photoUrl: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    val lat: Double,
    val lon: Double,
) : Parcelable