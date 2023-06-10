package com.capstone.repoth.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.cobaretrofit.utils.ResultState
import com.capstone.repoth.data.api.ApiService
import com.capstone.repoth.data.model.Post
import com.capstone.repoth.data.model.PredictResponse
import com.capstone.repoth.data.response.UploadRepothResponse
import com.google.android.gms.maps.model.LatLng
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class HelloRepository(private val apiService: ApiService) {

    suspend fun getHello(): Post {
        return apiService.getHello()
    }

    fun postPredict(
        image: MultipartBody.Part
    ): LiveData<ResultState<PredictResponse>> = liveData {
        try {
            emit(ResultState.Success(apiService.postPredict(image)))
        } catch (e: Exception){
            e.printStackTrace()
            Log.d(TAG, "addStory: ${e.message.toString()} ")
            emit(ResultState.Error(e.message.toString()))
        }
    }

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

        private val TAG = HelloRepository::class.java.simpleName

        @Volatile
        private var instance: HelloRepository? = null

        fun getInstance(apiService: ApiService): HelloRepository =
            instance ?: synchronized(this) {
                instance ?: HelloRepository(apiService)
            }.also { instance = it }

    }
}