package com.capstone.repoth.ui.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.cobaretrofit.utils.ResultState
import com.capstone.repoth.data.repository.HelloRepository
import com.google.android.gms.maps.model.LatLng
import okhttp3.RequestBody

class MapsViewModel(private val helloRepository: HelloRepository) : ViewModel() {

    fun uploadPredict(imageUrl: RequestBody, latLng: LatLng) = helloRepository.uploadPredict(imageUrl, latLng)

}