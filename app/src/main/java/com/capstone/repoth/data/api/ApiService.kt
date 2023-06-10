package com.capstone.repoth.data.api


import com.capstone.repoth.data.model.Post
import com.capstone.repoth.data.model.PredictResponse
import com.capstone.repoth.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @GET("/")
    suspend fun getHello(): Post

    @Multipart
     @POST("/predict")
     suspend fun postPredict(
        @Part image: MultipartBody.Part
     ): PredictResponse

    @GET("report")
    suspend fun getReport(
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): UserRepothResponse

    @GET("report/{id}")
    suspend fun getReportDetail(
        @Path("id") id: String
    ): RepothDetailResponse

    @POST("/uploads")
    @Multipart
    suspend fun uploadReport(
        @Part("imageUrl") imageUrl: RequestBody,
        @Part("latitude") latitude: Float?,
        @Part("longitude") longitude: Float?,
    ): UploadRepothResponse
}