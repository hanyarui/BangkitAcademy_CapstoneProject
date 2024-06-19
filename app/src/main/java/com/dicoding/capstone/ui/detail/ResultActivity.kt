package com.dicoding.capstone.ui.detail

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.data.PredictionResponse
import com.dicoding.capstone.data.service.ApiClient
import com.dicoding.capstone.databinding.ActivityResultBinding
import com.dicoding.capstone.util.BoundingBox
import com.dicoding.capstone.util.Detector
import com.dicoding.capstone.util.uriToFile
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultActivity : AppCompatActivity(), Detector.DetectorListener {
    private lateinit var binding: ActivityResultBinding
    private lateinit var detector: Detector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize detector
        detector = Detector(this, "yolov8n-face_float32.tflite", "labels.txt", this)
        detector.setup()

        val photoUri = intent.getStringExtra("photoUri")
        if (photoUri != null) {
            binding.ivResult.setImageURI(Uri.parse(photoUri))
            val bitmap = (binding.ivResult.drawable as BitmapDrawable).bitmap
            detector.detect(bitmap)

            binding.btnConfirm.setOnClickListener {
                sendFaceRecognitionRequest(Uri.parse(photoUri))
            }
        } else {
            Toast.makeText(this, "No Face Detected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.clear()
    }

    override fun onEmptyDetect() {
        binding.overlay.invalidate()
    }

    override fun onDetect(boundingBoxes: List<BoundingBox>, inferenceTime: Long) {
        runOnUiThread {
            binding.tvInferenceTime.text = "${inferenceTime}ms"
            binding.overlay.apply {
                setResults(boundingBoxes)
                invalidate()
            }
        }
    }

    private fun sendFaceRecognitionRequest(imageUri: Uri) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result?.token
                if (token != null) {
                    val file = uriToFile(imageUri, this)
                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                    ApiClient.instance.predictFace(body, "Bearer $token").enqueue(object : Callback<PredictionResponse> {
                        override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                            if (response.isSuccessful) {
                                val predictionResponse = response.body()
                                if (predictionResponse != null && predictionResponse.success) {
                                    Toast.makeText(this@ResultActivity, "Prediction: ${predictionResponse.data}", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@ResultActivity, "Failed: ${predictionResponse?.message}", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@ResultActivity, "Response unsuccessful", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                            Toast.makeText(this@ResultActivity, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(this, "Failed to get token", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Token retrieval failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
