package com.capstone.repoth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.capstone.repoth.R
import com.capstone.repoth.data.api.ApiConfig
import com.capstone.repoth.data.api.ApiService
import com.capstone.repoth.databinding.ActivityMainBinding
import com.capstone.repoth.ui.viewmodel.UploadRepothViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val uploadRepothViewModel: UploadRepothViewModel by inject()

    private lateinit var uploadRepothViewModel: UploadRepothViewModel<ApiConfig<String>>
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

}