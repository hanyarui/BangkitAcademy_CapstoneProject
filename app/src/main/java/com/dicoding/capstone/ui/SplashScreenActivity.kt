package com.dicoding.capstone.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.capstone.R
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.ui.auth.LoginActivity
import com.dicoding.capstone.ui.tabLayout.TabLayoutActivity
import com.dicoding.capstone.ui.slide.CameraPermissionActivity
import com.google.firebase.auth.FirebaseAuth
//import org.opencv.android.OpenCVLoader

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val SPLASH_DELAY = 3000L
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        userPreference = UserPreference(this)
        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextActivity()
        }, SPLASH_DELAY)

//        if (!OpenCVLoader.initDebug()) {
//            // Handle initialization error
//            println("OpenCV initialization failed")
//        } else {
//            println("OpenCV initialized successfully")
//        }
    }

    private fun navigateToNextActivity() {
        if (!allPermissionsGranted()) {
            startActivity(Intent(this, CameraPermissionActivity::class.java))
        } else {
            if (auth.currentUser != null) {
                startActivity(Intent(this, TabLayoutActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        finish()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}
