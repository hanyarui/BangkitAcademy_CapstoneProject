package com.dicoding.capstone.ui.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.data.response.PredictionResponse
import com.dicoding.capstone.data.service.ApiService
import com.dicoding.capstone.databinding.ActivityResultBinding
import com.dicoding.capstone.util.BoundingBox
import com.dicoding.capstone.util.Detector
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.ui.tabLayout.HomepageActivity

class ResultActivity : AppCompatActivity(), Detector.DetectorListener {

    private lateinit var binding: ActivityResultBinding
    private lateinit var detector: Detector
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)

        // Initialize detector
        detector = Detector(this, "yolov8n-face_float32.tflite", "labels.txt", this)
        detector.setup()

        val photoUri = intent.getStringExtra("photoUri")
        if (photoUri != null) {
            binding.ivResult.setImageURI(Uri.parse(photoUri))
            val bitmap = (binding.ivResult.drawable as BitmapDrawable).bitmap
            detector.detect(bitmap)
        } else {
            Toast.makeText(this, "No Face Detected", Toast.LENGTH_SHORT).show()
        }

        binding.btnConfirm.setOnClickListener {
            val bitmap = (binding.ivResult.drawable as BitmapDrawable).bitmap
            val token = userPreference.getToken()
            if (token != null) {
                sendFaceRecognitionRequest(bitmap, token)
            } else {
                Toast.makeText(this, "Failed to get user token", Toast.LENGTH_SHORT).show()
            }
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

    // Private method to convert Bitmap to MultipartBody.Part
    private fun bitmapToMultipartBody(bitmap: Bitmap): MultipartBody.Part {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), bytes.toByteArray())
        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }

    private fun sendFaceRecognitionRequest(bitmap: Bitmap, token: String) {
        Log.d("Activity Result", "token $token")

        // Convert Bitmap to MultipartBody.Part
        val body = bitmapToMultipartBody(bitmap)

        // Panggil API untuk prediksi wajah dengan mengirimkan file sebagai bagian multipart
        ApiService.instance.predict("Bearer $token", body).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    val predictionResponse = response.body()
                    if (predictionResponse != null) {
                        // Tampilkan hasil prediksi
                        for (predict in predictionResponse.result) {
                            Toast.makeText(this@ResultActivity, "Wajah terdeteksi: ${predict.className}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ResultActivity, HomepageActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this@ResultActivity, "Gagal mendapatkan hasil prediksi", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ResultActivity, "Respon tidak berhasil", Toast.LENGTH_SHORT).show()
                    Log.d("Result Activity", "Response: $response")
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                Toast.makeText(this@ResultActivity, "Permintaan gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
