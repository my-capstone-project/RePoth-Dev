package com.capstone.repoth.di

import com.capstone.repoth.data.api.ApiConfig
import com.capstone.repoth.data.repository.HelloRepository

object Injection {
    fun provideRepository(): HelloRepository {
        val apiService = ApiConfig.getApiService()
        return HelloRepository.getInstance(apiService)
    }

    fun provideRepositoryExpress(): HelloRepository {
        val apiService = ApiConfig.getApiServiceExpress()
        return HelloRepository.getInstance(apiService)
    }
}