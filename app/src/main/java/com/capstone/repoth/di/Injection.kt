package com.capstone.repoth.di

import com.capstone.repoth.data.api.ApiConfig
import com.capstone.repoth.data.api.ApiConfigExpress
import com.capstone.repoth.data.repository.HelloRepository
import com.capstone.repoth.data.repository.RepothRepository

object Injection {
    fun provideRepository(): HelloRepository {
        val apiService = ApiConfig.getApiService()
        return HelloRepository.getInstance(apiService)
    }

    fun provideRepositoryExpress(): RepothRepository {
        val apiService = ApiConfigExpress.getApiService()
        return RepothRepository.getInstance(apiService)
    }
}