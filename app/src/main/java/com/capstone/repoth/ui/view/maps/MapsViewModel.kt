package com.capstone.repoth.ui.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.cobaretrofit.utils.ResultState
import com.capstone.repoth.data.repository.HelloRepository
import com.capstone.repoth.data.repository.RepothRepository
import com.google.android.gms.maps.model.LatLng
import okhttp3.RequestBody

class MapsViewModel(private val repothRepository: RepothRepository) : ViewModel() {

    fun uploadPredict(imageUrl: RequestBody, username: RequestBody, latLng: LatLng) = repothRepository.uploadPredict(username, imageUrl, latLng)

}