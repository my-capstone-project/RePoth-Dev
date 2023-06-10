package com.capstone.repoth.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.repoth.data.model.Post
import com.capstone.repoth.data.repository.HelloRepository
import com.capstone.cobaretrofit.utils.ResultState
import kotlinx.coroutines.launch


class MainViewModel(private val helloRepository: HelloRepository) : ViewModel() {

    private val _hello = MutableLiveData<ResultState<Post>>()
    val hello: LiveData<ResultState<Post>>
        get() = _hello

    fun fetchData(){
        viewModelScope.launch {
            try {
                val response = helloRepository.getHello()
                _hello.value = ResultState.Success(response)
            } catch (e: Exception) {
                _hello.value = ResultState.Error("Error")
            }
        }
    }
}