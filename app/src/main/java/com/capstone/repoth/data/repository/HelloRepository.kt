package com.capstone.repoth.data.repository

import com.capstone.cobaretrofit.utils.Result
import com.capstone.repoth.data.api.ApiService
import com.capstone.repoth.data.model.Post

class HelloRepository(private val apiService: ApiService) {

    suspend fun getHello(): Post {
        return apiService.getHello()
    }

    companion object {

        @Volatile
        private var instance: HelloRepository? = null

        fun getInstance(apiService: ApiService): HelloRepository =
            instance ?: synchronized(this) {
                instance ?: HelloRepository(apiService)
            }.also { instance = it }

    }
}