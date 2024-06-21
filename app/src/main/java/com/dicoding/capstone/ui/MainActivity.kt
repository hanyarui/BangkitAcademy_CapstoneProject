package com.dicoding.capstone.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.ActivityMainBinding
import com.dicoding.capstone.ui.auth.LoginAdminActivity
import com.dicoding.capstone.ui.auth.LoginUserActivity
import com.dicoding.capstone.ui.auth.RegisterActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        toSignInUserPage()
        toSignInAdminPage()
        toSignUpPage()
    }

    private fun toSignInUserPage() {
        val toSignIn = findViewById<AppCompatButton>(R.id.btnSignInUser)
        toSignIn.setOnClickListener {
            val intent = Intent(this, LoginUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toSignInAdminPage() {
        val toSignIn = findViewById<AppCompatButton>(R.id.btnSignInAdmin)
        toSignIn.setOnClickListener {
            val intent = Intent(this, LoginAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toSignUpPage() {
        val toSignIn = findViewById<AppCompatButton>(R.id.btnSignUp)
        toSignIn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}