package com.dicoding.capstone.ui.classroom

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.R
import com.dicoding.capstone.util.ViewModelFactory
import com.dicoding.capstone.viewModel.JoinClassViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class JoinClassActivity : AppCompatActivity() {

    private lateinit var etClassCode: EditText
    private lateinit var btnJoin: Button

    private val joinClassViewModel: JoinClassViewModel by viewModels {
        ViewModelFactory(FirebaseAuth.getInstance(), null, null, FirebaseFirestore.getInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_class)

        etClassCode = findViewById(R.id.etKelas)
        btnJoin = findViewById(R.id.btnKonfirmasi)

        btnJoin.setOnClickListener {
            val classCode = etClassCode.text.toString().trim()
            if (classCode.isNotEmpty()) {
                joinClass(classCode)
            } else {
                Toast.makeText(this, "Please enter a class code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun joinClass(classCode: String) {
        joinClassViewModel.joinClass(classCode) { success, message ->
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
            if (success) {
                finish()
            }
        }
    }
}
