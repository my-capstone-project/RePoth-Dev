package com.capstone.repoth.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.repoth.data.model.Post
import com.capstone.repoth.data.repository.HelloRepository
import com.capstone.cobaretrofit.utils.Result
import kotlinx.coroutines.launch


class MainViewModel(private val helloRepository: HelloRepository) : ViewModel() {

    private val _hello = MutableLiveData<Result<Post>>()
    val hello: LiveData<Result<Post>>
        get() = _hello

    fun fetchData(){
        viewModelScope.launch {
            try {
                val response = helloRepository.getHello()
                _hello.value = Result.Success(response)
            } catch (e: Exception) {
                _hello.value = Result.Error("Error")
            }
        }
    }
}