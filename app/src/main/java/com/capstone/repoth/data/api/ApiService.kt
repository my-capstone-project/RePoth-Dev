package com.capstone.repoth.data.api


import com.capstone.repoth.data.model.Post
import com.capstone.repoth.data.model.PredictResponse
import com.capstone.repoth.data.response.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @GET("/")
    suspend fun getHello(): Post

    @Multipart
     @POST("/predict")
     suspend fun postPredict(
        @Part image: MultipartBody.Part
     ): PredictResponse
}