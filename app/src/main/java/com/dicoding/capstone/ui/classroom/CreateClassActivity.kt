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
import com.dicoding.capstone.ui.tabLayout.HomepageActivity
import com.dicoding.capstone.viewModel.CreateClassViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class CreateClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateClassBinding

    private lateinit var btnSave: Button
    private lateinit var etKelas: TextInputEditText
    private lateinit var etMapel: TextInputEditText

    // Inisialisasi ViewModel
    private val createClassViewModel: CreateClassViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val viewModel = CreateClassViewModel()
                viewModel.initialize(this@CreateClassActivity) // Inisialisasi dengan context
                @Suppress("UNCHECKED_CAST")
                return viewModel as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi View
        btnSave = findViewById(R.id.btnKonfirmasi)
        etKelas = findViewById(R.id.etKelas)
        etMapel = findViewById(R.id.etMapel)

        // Button Save
        btnSave.setOnClickListener {
            saveSchedule()
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
            students = emptyList(),
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


