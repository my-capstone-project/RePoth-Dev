package com.capstone.repoth.data.model

import com.google.gson.annotations.SerializedName

data class UserData(
    @field:SerializedName("token")
    var token: String
)