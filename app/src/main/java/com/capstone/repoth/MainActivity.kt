package com.capstone.repoth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.capstone.repoth.R
import com.capstone.repoth.data.api.ApiConfig
import com.capstone.repoth.data.api.ApiService
import com.capstone.repoth.databinding.ActivityMainBinding
import com.capstone.repoth.ui.viewmodel.UploadRepothViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val uploadRepothViewModel: UploadRepothViewModel by inject()
    private lateinit var uploadRepothResultViewModel: Observer<ApiService<String>>
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
    private fun isMainPage(currentDestination: NavDestination): Boolean {
        return currentDestination.id == R.id.homeActivity
                || currentDestination.id == R.id.detailRepothActivity
                || currentDestination.id == R.id.mapsActivity
    }

    private fun View.showSnackBar(message: String) {
        Snackbar.make(
            this,
            message,
            Snackbar.LENGTH_LONG
        ).also { snackbar ->
            snackbar.setAction("OK") {
                snackbar.dismiss()
            }
        }.show()
    }
}