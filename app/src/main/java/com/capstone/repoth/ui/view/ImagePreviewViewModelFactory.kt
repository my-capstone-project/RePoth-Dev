package com.capstone.repoth.ui.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.repoth.data.repository.HelloRepository
import com.capstone.repoth.di.Injection

class ImagePreviewViewModelFactory(
    private val repository: HelloRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ImagePreviewViewModel::class.java) -> {
                ImagePreviewViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ImagePreviewViewModelFactory? = null
        fun getInstance(context: Context): ImagePreviewViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ImagePreviewViewModelFactory(
                    Injection.provideRepository()
                )
            }.also { instance = it }
    }
}