package com.capstone.repoth.ui.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import com.capstone.cobaretrofit.utils.ResultState
import com.capstone.repoth.MainActivity
import com.capstone.repoth.databinding.ActivityImagePreviewBinding
import com.capstone.repoth.helper.reduceFileImage
import com.capstone.repoth.helper.rotateBitmap
import com.capstone.repoth.helper.uriToFile
import com.capstone.repoth.ui.login.LoginActivity
import com.capstone.repoth.ui.view.camera.CameraActivity
import com.capstone.repoth.ui.view.maps.MapsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImagePreviewActivity: AppCompatActivity() {

    private lateinit var binding: ActivityImagePreviewBinding
    private lateinit var imagePreviewViewModel: ImagePreviewViewModel
    private lateinit var auth: FirebaseAuth
    private var getFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val imagePreviewViewModelFactory: ImagePreviewViewModelFactory = ImagePreviewViewModelFactory.getInstance(this)
        imagePreviewViewModel = ViewModelProvider(
            this, imagePreviewViewModelFactory
        )[ImagePreviewViewModel::class.java]

        // Get uri image from previous
        val uri = Uri.parse(getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("pathimage", null))
        if (uri != null){
            // get file binary
            val myFile = uriToFile(uri, this@ImagePreviewActivity)
            rotateBitmap(myFile)
            getFile = myFile

            // set into activity layout
            binding.imageTakenPreviewContainer.setImageURI(uri)

            // button upload
            binding.pictureSendBtn.setOnClickListener{
                uploadImage()
            }
        }

        binding.retakeBtn.setOnClickListener {
            // Back to CameraActivity Scene
            startActivity(Intent(this, CameraActivity::class.java))
            finish()
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

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )
            imagePreviewViewModel.uploadImage(imageMultipart).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            binding.loading.visibility = View.VISIBLE
                        }
                        is ResultState.Success -> {
                            binding.loading.visibility = View.INVISIBLE
                            Toast.makeText(this, result.data.message, Toast.LENGTH_LONG).show()
                            val predict = result.data.result;
                            Log.d(ImagePreviewActivity::class.java.simpleName, "" +
                                    "Filename: ${predict.filename}\n" +
                                    "Pothole: ${predict.pothole}\n" +
                                    "Url: ${predict.url}$")

                            if (predict.pothole){
                                // Move to MapsActivity and send the URL
                                getSharedPreferences("Settings", Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("imageurl", predict.url)
                                    .apply()
                                startActivity(Intent(this, MapsActivity::class.java))
                                finish()

                            } else {
                                // Move to HomeActivity
                                val intent = Intent(this, DetailRepothActivity::class.java)
                                intent.putExtra("SUCCESS", false)
                                startActivity(intent)
                                finish()
                            }
                        }
                        is ResultState.Error -> {
                            binding.loading.visibility = View.INVISIBLE
                            Toast.makeText(this, "Failure : " + result.error, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@ImagePreviewActivity,
                        "Silakan masukkan gambar terlebih dahulu.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}