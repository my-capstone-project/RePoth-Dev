package com.capstone.repoth.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.repoth.data.model.UserPreference

class SplashViewModel(private val pref: UserPreference) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun getBoardingPage(): LiveData<Boolean> {
        return pref.getBoardingPage().asLiveData()
    }
}