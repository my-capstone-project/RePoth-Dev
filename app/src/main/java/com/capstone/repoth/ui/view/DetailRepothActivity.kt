package com.capstone.repoth.ui.view

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.capstone.repoth.R
import com.capstone.repoth.databinding.ActivityDetailRepothBinding
import com.capstone.repoth.helper.preferenceReset

class DetailRepothActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRepothBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRepothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Action bar
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Info"

        var success = getSharedPreferences("Settings", Context.MODE_PRIVATE).getBoolean("success", false)
        var info = ""
        var color: Int

        if (success){
            color = R.color.success
            info = "Teruskan laporan jalan berlubang lainnya untuk memajukan infrastruktur indonesia :)"

            // Get Kecamatan, Kabupaten
            var kecamatan = ""
            var kota = ""

            val latitude = intent.getDoubleExtra("latitude", 0.0)
            val longitude = intent.getDoubleExtra("longitude", 0.0)

            val geocoder = Geocoder(this)
            val test = geocoder.getFromLocation(latitude, longitude, 1)
            if (test != null) {
                for (t in test){

                    kecamatan = t.locality
                    kota = t.subAdminArea
                }
            }

            binding.location.visibility = View.VISIBLE
            binding.location.text = "Location ${kecamatan}, ${kota}"

        } else {
            color = R.color.fail
            info = "Maaf karena jalanan tidak diprioritaskan sebagai jalan berlubang :("

            // Remove location
            binding.location.visibility = View.INVISIBLE
        }

        // Reset Preference
        preferenceReset(this)

        binding.info.setBackgroundColor(getColor(color))
        binding.info.text = info
    }
}