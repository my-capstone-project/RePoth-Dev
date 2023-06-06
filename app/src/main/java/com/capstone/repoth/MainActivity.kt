package com.capstone.repoth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.capstone.cobaretrofit.utils.Result
import com.capstone.repoth.data.repository.HelloRepository
import com.capstone.repoth.databinding.ActivityMainBinding
import com.capstone.repoth.ui.viewmodel.MainViewModel
import com.capstone.repoth.ui.viewmodel.MainViewModelFactory
import com.capstone.repoth.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mainViewModelFactory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        viewModel.hello.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val response = result.data
                    binding.textHello.text = response.hello
                }
                is Result.Error -> {
                    val exception = result.error
                    Toast.makeText(
                        this@MainActivity,
                        exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
        viewModel.fetchData()
    }

}