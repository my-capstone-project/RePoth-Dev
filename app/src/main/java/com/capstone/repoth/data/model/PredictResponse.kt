package com.capstone.repoth.data.model

data class PredictResponse(
    var error: Boolean,
    var message: String,
    var result: ResultData
)

data class ResultData(
    var filename: String,
    var pothole: Boolean,
    var url: String
)