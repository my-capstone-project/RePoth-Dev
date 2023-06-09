package com.capstone.repoth.data.api


import com.capstone.repoth.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("report")
    suspend fun getReport(
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): UserRepothResponse

    @GET("report/{id}")
    suspend fun getReportDetail(
        @Path("id") id: String
    ): RepothDetailResponse

    @POST("report")
    @Multipart
    suspend fun uploadRepoth(
        @Part photo: MultipartBody.Part,
        @Part ("description") description: RequestBody,
        @Part("lat") latitude: RequestBody? = null,
        @Part("lon") longitude: RequestBody? = null,
    ): UploadRepothResponse

}