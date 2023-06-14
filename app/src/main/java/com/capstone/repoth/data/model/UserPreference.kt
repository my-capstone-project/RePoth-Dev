package com.capstone.repoth.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getBoardingPage(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[BOARDING_KEY] ?: true
        }
    }

    suspend fun saveBoardingPage(setData : Boolean ){
        dataStore.edit { preferences ->
            preferences[BOARDING_KEY] = setData

        }
    }
}