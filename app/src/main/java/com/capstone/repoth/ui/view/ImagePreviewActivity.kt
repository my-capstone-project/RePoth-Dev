package com.capstone.repoth.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.cobaretrofit.utils.ResultState
import com.capstone.repoth.databinding.ActivityImagePreviewBinding
import com.capstone.repoth.helper.reduceFileImage
import com.capstone.repoth.helper.rotateBitmap
import com.capstone.repoth.helper.uriToFile
import com.capstone.repoth.ui.view.camera.CameraActivity
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
//                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is ResultState.Success -> {
//                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, result.data.message, Toast.LENGTH_LONG).show()
                            val predict = result.data.result;
                            Log.d(ImagePreviewActivity::class.java.simpleName, "" +
                                    "Filename: ${predict.filename}\n" +
                                    "Pothole: ${predict.pothole}\n" +
                                    "Url: ${predict.url}$")
//                            startActivity(
//                                Intent(this, StoryActivity::class.java)
//                            )
//                            finish()
                        }
                        is ResultState.Error -> {
//                            binding.progressBar.visibility = View.GONE
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