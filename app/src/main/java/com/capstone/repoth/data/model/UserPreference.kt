package com.capstone.repoth.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }
    fun getBoardingPage(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[BOARDING_KEY] ?: true
        }
    }

    suspend fun saveBoardingPage(setData: Boolean) {
        dataStore.edit { preferences ->
            preferences[BOARDING_KEY] = setData

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
        private val BOARDING_KEY = booleanPreferencesKey("boarding_page")
        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}