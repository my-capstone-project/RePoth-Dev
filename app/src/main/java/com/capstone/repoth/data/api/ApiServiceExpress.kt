package com.capstone.repoth.data.api


import com.capstone.repoth.data.model.Post
import com.capstone.repoth.data.model.PredictResponse
import com.capstone.repoth.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiServiceExpress {

    @POST("/uploads")
    @Multipart
    suspend fun uploadReport(
        @Part("username") username: String,
        @Part("imageUrl") imageUrl: RequestBody,
        @Part("latitude") latitude: Float?,
        @Part("longitude") longitude: Float?,
    ): UploadRepothResponse
}