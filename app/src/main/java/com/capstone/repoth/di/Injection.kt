package com.capstone.repoth.di

import android.content.Context
import com.capstone.repoth.data.repository.RepothRepository
import com.capstone.repoth.data.api.ApiConfig
import com.capstone.repoth.database.RepothDatabase

object Injection {
    fun provideRepository(context: Context): RepothRepository {
        val database = RepothDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return RepothRepository(database, apiService)
    }
}