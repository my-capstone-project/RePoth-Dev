package com.capstone.repoth.data.response

import com.capstone.repoth.data.model.ResultData
import com.google.gson.annotations.SerializedName

data class UploadRepothResponse (
    @SerializedName("error")
    var error: Boolean,

    @SerializedName("message")
    var message: String
)