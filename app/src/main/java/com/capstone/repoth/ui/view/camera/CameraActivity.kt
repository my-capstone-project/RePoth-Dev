package com.capstone.repoth.ui.view.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.repoth.MainActivity
import com.capstone.repoth.databinding.ActivityCameraBinding
import com.capstone.repoth.helper.createFile
import com.capstone.repoth.helper.rotateBitmap
import com.capstone.repoth.helper.showToast
import com.capstone.repoth.helper.updateTimeStamp
import com.capstone.repoth.helper.uriToFile
import com.capstone.repoth.ui.login.LoginActivity
import com.capstone.repoth.ui.view.ImagePreviewActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.File

class CameraActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityCameraBinding
    private lateinit var outputDirectory: File
    private lateinit var auth: FirebaseAuth

    private var imageCapture: ImageCapture? = null
    private var back = true

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 123
        private val TAG = CameraActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // If camera permission is allowed
        if (allPermissionGranted()){
            startCamera()
            // Toast.makeText(this, "We Have Permission", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.captureImage.setOnClickListener {
            updateTimeStamp()
            outputDirectory = createFile(application)
            takePhoto()
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

    //
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (allPermissionGranted()){
                //our code
                startCamera()

            } else {
                Toast.makeText(this, "Permission not granted by user", Toast.LENGTH_SHORT).show()

                // Back to dashboard
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    // Camera methods
    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector: CameraSelector
            if(back){
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            } else {
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            }

            try {

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    preview, imageCapture
                )

            } catch (e: Exception){
                Log.d(TAG, "startCamera Fail", e)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto(){

        val imageCapture = imageCapture ?: return
        val photoFile = outputDirectory
        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object: ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val saveUri = Uri.fromFile(photoFile)
                    val msg = "Photo saved"

                    showToast(this@CameraActivity, "$msg $saveUri")

                    getSharedPreferences("Settings", Context.MODE_PRIVATE)
                        .edit()
                        .putString("pathimage", saveUri.toString())
                        .apply()

                    startActivity(Intent(this@CameraActivity, ImagePreviewActivity::class.java))
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "onError: ${exception.message}", exception)
                }
            }
        )
    }

    // Check permission for camera/video
    private fun allPermissionGranted(): Boolean{
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}