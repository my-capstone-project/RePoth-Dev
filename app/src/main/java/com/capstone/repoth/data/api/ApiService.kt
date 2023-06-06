package com.capstone.repoth.data.api


import com.capstone.cobaretrofit.utils.Result
import com.capstone.repoth.data.model.Post
import com.capstone.repoth.data.response.*
import retrofit2.http.*

interface ApiService {
    @GET("/")
    suspend fun getHello(): Post

    // @POST("/predict")
    // suspend fun postPredict():
}