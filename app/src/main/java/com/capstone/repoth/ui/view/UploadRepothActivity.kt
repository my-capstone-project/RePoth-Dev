package com.capstone.repoth.ui.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.repoth.R
import com.capstone.repoth.data.model.UserData
import com.capstone.repoth.data.model.UserPreferences
import com.capstone.repoth.databinding.ActivityUploadRepothBinding
import com.capstone.repoth.helper.*
import com.capstone.repoth.ui.viewmodel.UploadRepothViewModel
import com.capstone.repoth.ui.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadRepothActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityUploadRepothBinding
    private var getFile: File? = null
    private var result: Bitmap? = null
    private lateinit var uploadStoryViewModel: UploadRepothViewModel
    private lateinit var user: UserData

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.invalid_permission), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadRepothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.upload_repoth)
            setDisplayHomeAsUpEnabled(true)
        }

        uploadRepothViewModel = ViewModelProvider(this, ViewModelFactory(UserPreferences.getInstance(dataStore),this))[UploadRepothViewModel::class.java]

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun startCameraX() {
        launcherIntentCameraX.launch(Intent(this, CameraActivity::class.java))
    }

    private val launcherIntentCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result = rotateBitmap(BitmapFactory.decodeFile(getFile?.path), isBackCamera)
        }
        binding.imgPreview.setImageBitmap(result)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose Image")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadRepothActivity)
            getFile = myFile
            binding.imgPreview.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description = binding.description.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            uploadStoryViewModel.getUser().observe(this) { user ->
                this.user = user
                uploadStoryViewModel.uploadImage(user.token, imageMultipart)
                uploadStoryViewModel.fileUploadResponse.observe(this@UploadRepothActivity) { fileUploadResponse ->
                    if (!fileUploadResponse.error) {
                        binding.btnUpload.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                        Toast.makeText(this@UploadRepothActivity, fileUploadResponse.message, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        progressBar()
                        binding.btnUpload.visibility = View.VISIBLE
                        Toast.makeText(this@UploadRepothActivity, fileUploadResponse.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this@UploadRepothActivity, getString(R.string.no_attach_file), Toast.LENGTH_SHORT).show()
        }
    }

    private fun progressBar() {
        uploadStoryViewModel.isLoading.observe(this) {
            binding.apply {
                if (it){
                    btnUpload.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                }
                else progressBar.visibility = View.GONE
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}