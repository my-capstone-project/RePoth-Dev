package com.capstone.repoth.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.repoth.databinding.ActivityDetailRepothBinding

class DetailRepothActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRepothBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRepothBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}