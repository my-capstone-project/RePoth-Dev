package com.capstone.repoth.data.repository

import com.capstone.repoth.data.api.ApiServiceExpress

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.cobaretrofit.utils.ResultState
import com.capstone.repoth.data.response.UploadRepothResponse
import com.google.android.gms.maps.model.LatLng
import okhttp3.RequestBody

class RepothRepository(private val apiService: ApiServiceExpress) {

    fun uploadPredict(
        url: RequestBody,
        latLng: LatLng?
    ): LiveData<ResultState<UploadRepothResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val lat = latLng?.latitude?.toFloat()
            val lng = latLng?.longitude?.toFloat()
            val newStory = apiService.uploadReport(url, lat, lng)
            emit(ResultState.Success(newStory))
        } catch (e: Exception){
            e.printStackTrace()
            Log.d(TAG, "uploadRepoth: ${e.message.toString()} ")
            emit(ResultState.Error(e.message.toString()))
        }
    }

    companion object {

        private val TAG = RepothRepository::class.java.simpleName

        @Volatile
        private var instance: RepothRepository? = null

        fun getInstance(apiService: ApiServiceExpress): RepothRepository =
            instance ?: synchronized(this) {
                instance ?: RepothRepository(apiService)
            }.also { instance = it }

    }
}