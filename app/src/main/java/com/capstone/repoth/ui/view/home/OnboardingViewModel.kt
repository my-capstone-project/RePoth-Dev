package com.capstone.repoth.ui.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.repoth.data.model.UserPreference
import kotlinx.coroutines.launch

class OnboardingViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveBoardingPage(data: Boolean) {
        viewModelScope.launch {
            pref.saveBoardingPage(data)
        }
    }
}