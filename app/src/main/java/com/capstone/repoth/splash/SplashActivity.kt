package com.capstone.repoth.splash

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.capstone.repoth.MainActivity
import com.capstone.repoth.databinding.ActivitySplashBinding
import com.capstone.repoth.ui.view.home.OnboardingActivity

class SplashActivity: AppCompatActivity(){

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            // Check if we need to display our OnboardingSupportFragment
            if (!getBoolean(OnboardingActivity::class.java.simpleName, false)) {
                // The user hasn't seen the OnboardingSupportFragment yet, so show it
                startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                finish()
            }
        }

        // Start into Main
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}