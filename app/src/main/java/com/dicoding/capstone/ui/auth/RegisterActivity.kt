package com.dicoding.capstone.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.databinding.ActivityRegisterBinding
import com.dicoding.capstone.ui.MainActivity
import com.dicoding.capstone.util.ViewModelFactory
import com.dicoding.capstone.viewModel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: AuthViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup role spinner
        val roles = arrayOf("student", "teacher") // Replace with your roles
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sRole.adapter = adapter

        binding.btnSignUp.setOnClickListener {
            val username = binding.etUserName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPass.text.toString().trim()
            val repeatPassword = binding.etRepass.text.toString().trim()
            val role = binding.sRole.selectedItem.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password == repeatPassword) {
                registerViewModel.register(username, email, password, role)
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.registerResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                // Navigate to next activity or login
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.onFailure {
                Toast.makeText(this, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
