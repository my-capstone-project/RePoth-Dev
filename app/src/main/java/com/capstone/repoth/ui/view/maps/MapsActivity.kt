package com.capstone.repoth.ui.view.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.cobaretrofit.utils.ResultState
import com.capstone.repoth.MainActivity
import com.capstone.repoth.R
import com.capstone.repoth.databinding.ActivityMapsBinding
import com.capstone.repoth.helper.showToast
import com.capstone.repoth.ui.login.LoginActivity
import com.capstone.repoth.ui.view.DetailRepothActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: LatLng
    private lateinit var auth: FirebaseAuth

    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.button.visibility = View.INVISIBLE

        val mapsViewModelFactory: MapsViewModelFactory = MapsViewModelFactory.getInstance(this)
        mapsViewModel = ViewModelProvider(
            this, mapsViewModelFactory
        )[MapsViewModel::class.java]

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.d("Mappp", "OnCreate")

        lastLocation = LatLng(0.0,0.0) // Default location

        imageUrl = getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("imageurl", null)

        binding.button.setOnClickListener {
            // for testing
            // imageUrl = "https://storage.googleapis.com/potholeimages/c43fc8c1aa9dcb93364dfd42db2e8252.jpg"
            if (imageUrl != null){

                var username = getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("username", null)
                if (username == null){
                    Firebase.auth.signOut()
                    checkUser()
                }

                mapsViewModel.uploadPredict(imageUrl.toString().toRequestBody("text/plain".toMediaType()), username.toString().toRequestBody("text/plain".toMediaType()), lastLocation).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> {

                            }

                            is ResultState.Success -> {
                                Log.d("Mappp", result.data.message)
                                getSharedPreferences("Settings", Context.MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("success", true)
                                    .apply()
                                startActivity(Intent(this, DetailRepothActivity::class.java))
                                finish()
                            }

                            is ResultState.Error -> {

                                Toast.makeText(
                                    this,
                                    "Failure : " + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }else {
                Log.d("Mappp", "imageUrl not found")
                Toast.makeText(this, "URL not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        checkUser()
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Settings
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.isTrafficEnabled = false
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true

        setupView()
        getMyLocation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->

            // Calling Location Manager
            val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // Checking GPS is enabled
            val mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if(!mGPS){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            if (isGranted) {
                getMyLocation()
            }else{
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
            mMap.isMyLocationEnabled = true
            mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null){
                    Log.d("Mappp", "Latitude ${location.latitude}")
                    Log.d("Mappp", "Longitude ${location.longitude}")
                    lastLocation = LatLng(location.latitude, location.longitude)

                    val geocoder = Geocoder(this)
                    val test = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (test != null) {
                        for (t in test){
                            val text = "PostalCode: ${t.postalCode}\n" +
                                    "CountryName: ${t.countryName}\n" +
                                    "Locality: ${t.locality}\n" +
                                    "AdminArea: ${t.adminArea}\n" +
                                    "FeatureName: ${t.featureName}\n" +
                                    "Phone: ${t.phone}\n" +
                                    "Premises: ${t.premises}\n" +
                                    "SubAdminArea: ${t.subAdminArea}\n" +
                                    "SubLocality: ${t.subLocality}\n" +
                                    "SubThoroughfare: ${t.subThoroughfare}\n" +
                                    "Thoroughfare: ${t.thoroughfare}\n" +
                                    "Url: ${t.url}\n"
                            Log.d("mappp", text)
                        }
                    }

                    mMap.addMarker(
                        MarkerOptions()
                            .position(lastLocation)
                            .title("Marker"));

                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            lastLocation,
                            17f
                        )
                    )

                    binding.button.visibility = View.VISIBLE
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


}