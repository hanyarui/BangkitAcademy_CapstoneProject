package com.dicoding.capstone.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.databinding.ActivityLoginBinding
import com.dicoding.capstone.ui.forgotPassword.ForgotPasswordActivity
import com.dicoding.capstone.ui.tabLayout.TabLayoutActivity
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.util.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    // View Binding Declaration
    private lateinit var binding: ActivityLoginBinding

    // ViewModel Declaration
    private val viewModel: AuthViewModel by viewModels {
        val userPreference = UserPreference(this)
        ViewModelFactory(FirebaseAuth.getInstance(), userPreference)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        moveToForgotPassword()
        moveToRegistrationPage()
        setButtonOnClick()
    }

    private fun moveToForgotPassword() {
        binding.tvForgot.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun moveToRegistrationPage() {
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setButtonOnClick() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPass.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Semua field harus diisi")
                return@setOnClickListener
            }

            viewModel.loginUser(email, password) { success, _ ->
                if (success) {
                    showToast("Login berhasil")
                    val intent = Intent(this, TabLayoutActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showToast("Login gagal: Email atau Password salah")
                }
            }
        }
    }

    private fun showToast(message: String) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
