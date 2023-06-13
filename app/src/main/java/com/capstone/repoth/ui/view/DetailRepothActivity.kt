package com.capstone.repoth.ui.view

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.capstone.repoth.R
import com.capstone.repoth.databinding.ActivityDetailRepothBinding

class DetailRepothActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRepothBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRepothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var success = intent.getBooleanExtra("SUCCESS", false)

        val color: Int

        color = if (success){
            R.color.success
        } else {
            R.color.fail
        }

        binding.root.setBackgroundColor(getColor(color))
    }
}