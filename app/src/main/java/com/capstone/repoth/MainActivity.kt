package com.capstone.repoth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.capstone.repoth.databinding.ActivityMainBinding
import com.capstone.repoth.ui.login.LoginActivity
import com.capstone.repoth.ui.view.camera.CameraActivity
import com.capstone.repoth.ui.view.maps.MapsActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Report button click
        binding.report.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        //-----------------------------------------------------------------
        // Button for testing only
        // Testing map
        binding.test.setOnClickListener {
            getSharedPreferences("Settings", Context.MODE_PRIVATE)
                .edit()
                .putString("imageurl", "https://storage.googleapis.com/potholeimages/0b0010cf4cb0c5c2137c9dd0c7b1ee4f.jpg")
                .apply()
            startActivity(Intent(this, MapsActivity::class.java))
        }
        // Logout account
        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            checkUser()
        }
        //-----------------------------------------------------------------

    }

    override fun onStart() {
        super.onStart()

        checkUser()

        // If user has been login

        // Check if gps enable
        checkGPS()

        // Check location permission
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // GetCurrentLocation for checking first
        getMyLocation()
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

    // Check GPS
    private fun checkGPS(){
        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task
            .addOnFailureListener {e ->
                val statusCode = (e as ResolvableApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                    try {
                        e.startResolutionForResult(this, 100)
                    } catch (sendEx: IntentSender.SendIntentException) {

                    }
                }
            }
    }

    // getMyLocation
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->

            // Calling Location Manager
            val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // Checking GPS is enabled
            val mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // If GPS not available
            if(!mGPS){
                finish()
            }

            // If permission not granted
            if (!isGranted) {

                // Force user go to setting permission
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
        }
    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Check user account
    private fun checkUser(){
        // Check if user is signed in (non-null) and update UI accordingly.
        var currentUser = auth.getCurrentUser()
        Log.d("Loginnn", "currentUser: ${currentUser}")

        // If user hasn't login then force to Login
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}