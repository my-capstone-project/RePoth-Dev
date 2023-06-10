package com.capstone.repoth.data.model

import com.google.gson.annotations.SerializedName

data class PredictResponse(
    @SerializedName("error")
    var error: Boolean,

    @SerializedName("message")
    var message: String,

    @SerializedName("result")
    var result: ResultData
)

data class ResultData(
    var filename: String,
    var pothole: Boolean,
    var url: String
)