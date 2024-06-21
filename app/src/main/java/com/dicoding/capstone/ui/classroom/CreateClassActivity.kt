package com.dicoding.capstone.ui.classroom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.ActivityCreateClassBinding
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.data.service.ApiService
import com.dicoding.capstone.ui.tabLayout.HomepageActivity
import com.dicoding.capstone.util.ViewModelFactory
import com.dicoding.capstone.viewModel.CreateClassViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class CreateClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateClassBinding

    private lateinit var btnSave: Button
    private lateinit var etKelas: TextInputEditText
    private lateinit var etMapel: TextInputEditText

    // Inisialisasi ViewModel dengan ViewModelFactory
    private val createClassViewModel: CreateClassViewModel by viewModels {
        ViewModelFactory(
            context = this,
            userPreference = UserPreference(this),
            apiService = ApiService.instance
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi View
        btnSave = findViewById(R.id.btnKonfirmasi)
        etKelas = findViewById(R.id.etKelas)
        etMapel = findViewById(R.id.etMapel)

        // Set Action Bar Title with Username
        setActionBarTitleWithUsername()

        // Button Save
        btnSave.setOnClickListener {
            saveSchedule()
        }
    }

    private fun setActionBarTitleWithUsername() {
        val userPreference = UserPreference(this)
        val username = userPreference.getUserName()
        if (username != null) {
            supportActionBar?.title = "Welcome, $username"
        } else {
            supportActionBar?.title = "Welcome"
        }
    }

    private fun saveSchedule() {
        val className = etKelas.text.toString()
        val subject = etMapel.text.toString()

        if (className.isEmpty() || subject.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        createClassViewModel.saveSchedule(
            context = this,
            className = className,
            students = emptyList(), // Sesuaikan jika ada daftar siswa
            subject = subject
        ) { success, message ->
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                Log.d("CreateClassActivity", "Save schedule result: $message")
                if (success) {
                    startActivity(Intent(this, HomepageActivity::class.java))
                }
            }
        }
    }
}



