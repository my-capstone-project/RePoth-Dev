package com.capstone.repoth.ui.view.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.repoth.data.repository.HelloRepository
import com.capstone.repoth.di.Injection
import com.capstone.repoth.ui.view.ImagePreviewViewModel
import com.capstone.repoth.ui.view.ImagePreviewViewModelFactory

class MapsViewModelFactory(
    private val helloRepository: HelloRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(helloRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: MapsViewModelFactory? = null
        fun getInstance(context: Context): MapsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MapsViewModelFactory(
                    Injection.provideRepositoryExpress()
                )
            }.also { instance = it }
    }
}