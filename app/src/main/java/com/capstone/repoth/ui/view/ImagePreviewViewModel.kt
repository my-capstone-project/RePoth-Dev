package com.capstone.repoth.ui.view

import androidx.lifecycle.ViewModel
import com.capstone.repoth.data.repository.HelloRepository
import okhttp3.MultipartBody

class ImagePreviewViewModel(
    private val helloRepository: HelloRepository
): ViewModel() {

    fun uploadImage(image: MultipartBody.Part) =
        helloRepository.postPredict(image)
}