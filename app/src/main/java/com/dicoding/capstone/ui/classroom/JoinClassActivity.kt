package com.dicoding.capstone.ui.classroom

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.data.service.ApiService
import com.dicoding.capstone.databinding.ActivityJoinClassBinding
import com.dicoding.capstone.ui.tabLayout.HomepageActivity
import com.dicoding.capstone.util.ViewModelFactory
import com.dicoding.capstone.viewModel.JoinClassViewModel

class JoinClassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinClassBinding
    private lateinit var userPreference: UserPreference

    private val viewModel: JoinClassViewModel by viewModels {
        ViewModelFactory(null, userPreference, ApiService.instance )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup View Binding
        binding = ActivityJoinClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UserPreference
        userPreference = UserPreference(this)

        // Set up the join button click listener
        binding.btnKonfirmasi.setOnClickListener {
            val classCode = binding.etKelas.text.toString()
            if (classCode.isNotEmpty()) {
                joinClass(classCode)
            } else {
                Toast.makeText(this, "Please enter a class code", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe the joinClassResult LiveData
        viewModel.joinClassResult.observe(this) { response ->
            response?.let {
                if (it.success) {
                    Toast.makeText(this, "Failed to join the class", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Successfully joined the class", Toast.LENGTH_LONG).show()
                    intent = Intent(this, HomepageActivity::class.java)
                }
            }
        }
    }

    private fun joinClass(classCode: String) {
        viewModel.joinClass(classCode)
    }
}
