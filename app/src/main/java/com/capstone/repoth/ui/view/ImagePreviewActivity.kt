package com.capstone.repoth.ui.view

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
import com.capstone.repoth.ui.view.camera.CameraActivity
import com.capstone.repoth.ui.view.maps.MapsActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImagePreviewActivity: AppCompatActivity() {

    private lateinit var binding: ActivityImagePreviewBinding
    private lateinit var imagePreviewViewModel: ImagePreviewViewModel
    private var getFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imagePreviewViewModelFactory: ImagePreviewViewModelFactory = ImagePreviewViewModelFactory.getInstance(this)
        imagePreviewViewModel = ViewModelProvider(
            this, imagePreviewViewModelFactory
        )[ImagePreviewViewModel::class.java]

        var uri = Uri.parse(intent.getStringExtra("URI"))
        uri.let {
            val myFile = uriToFile(uri, this@ImagePreviewActivity)
            rotateBitmap(myFile)
            getFile = myFile
            binding.imageTakenPreviewContainer.setImageURI(uri)

            binding.pictureSendBtn.setOnClickListener{
                binding.loading.visibility = View.VISIBLE
                uploadImage()
            }
        }

        binding.retakeBtn.setOnClickListener {
            // Back to CameraActivity Scene
            startActivity(Intent(this, CameraActivity::class.java))
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
//                            binding.loading.isInvisible = false
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
                                val intent = Intent(this, MapsActivity::class.java)
                                intent.putExtra("MAP_URL", predict.url)
                                startActivity(intent)
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
//                            binding.loading.isInvisible = false
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