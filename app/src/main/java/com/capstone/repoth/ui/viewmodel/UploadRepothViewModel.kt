package com.capstone.repoth.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.repoth.data.model.UserPreferences
import com.capstone.repoth.data.api.ApiConfig
import com.capstone.repoth.data.response.UploadRepothResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadRepothViewModel(private val preferences: UserPreferences) : ViewModel() {

    private val _fileUploadResponse = MutableLiveData<UploadRepothResponse>()
    val fileUploadResponse: LiveData<UploadRepothResponse> = _fileUploadResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadImage(token: String, description: RequestBody, imageMultipart: MultipartBody.Part) {

        _isLoading.value = true
        val service = ApiConfig.getApiService().uploadRepoth("Bearer $token", imageMultipart)
        service.enqueue(object : Callback<UploadRepothResponse> {
            override fun onResponse(
                call: Call<UploadRepothResponse>,
                response: Response<UploadRepothResponse>
            ) {
                if (response.isSuccessful) {
                    _fileUploadResponse.value = response.body()
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    _fileUploadResponse.value =
                        UploadRepothResponse(jsonObj.getBoolean("error"), jsonObj.getString("message"))
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadRepothResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "UploadRepothViewModel"
    }
}