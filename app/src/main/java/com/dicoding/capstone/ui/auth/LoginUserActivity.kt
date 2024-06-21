package com.dicoding.capstone.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.databinding.ActivityLoginUserBinding
import com.dicoding.capstone.ui.tabLayout.HomepageActivity
import com.dicoding.capstone.util.ViewModelFactory
import com.dicoding.capstone.viewModel.AuthViewModel

class LoginUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginUserBinding
    private val loginViewModel: AuthViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {
            val username = binding.etEmail.text.toString().trim()
            val password = binding.etPass.text.toString().trim()

            loginViewModel.loginUser(username, password)
        }

        loginViewModel.loginResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                // Navigate to next activity or home
                val intent = Intent(this, HomepageActivity::class.java)
                startActivity(intent)
            }.onFailure {
                Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
