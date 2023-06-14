package com.capstone.repoth.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.capstone.repoth.MainActivity
import com.capstone.repoth.databinding.ActivitySplashBinding
import com.capstone.repoth.ui.view.home.OnboardingActivity

class SplashActivity: AppCompatActivity(){

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashScreenViewModel
    private val times = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        viewModel.getBoardingPage().observe(this){
            if(it){
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SplashScreenActivity, LandingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }, times)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }, times)
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SplashScreenViewModel::class.java]
        viewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}