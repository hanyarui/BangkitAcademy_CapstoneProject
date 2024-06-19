package com.dicoding.capstone.ui.classroom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.R
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.databinding.ActivityCreateClassBinding
import com.dicoding.capstone.ui.tabLayout.TabLayoutActivity
import com.dicoding.capstone.util.ViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class CreateClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateClassBinding

    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var cbRepeatWeekly: CheckBox
    private lateinit var btnSave: Button
    private lateinit var etKelas: TextInputEditText
    private lateinit var etMapel: TextInputEditText

    private var selectedDate: Calendar? = null
    private var selectedTime: Calendar? = null

    private val createClassViewModel: CreateClassViewModel by viewModels {
        ViewModelFactory(FirebaseAuth.getInstance(), UserPreference(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvDate = findViewById(R.id.tvDate)
        tvTime = findViewById(R.id.tvTime)
        cbRepeatWeekly = findViewById(R.id.cbRepeatWeekly)
        btnSave = findViewById(R.id.btnKonfirmasi)
        etKelas = findViewById(R.id.etKelas)
        etMapel = findViewById(R.id.etMapel)

        findViewById<Button>(R.id.btnDatePicker).setOnClickListener {
            pickDate()
        }

        findViewById<Button>(R.id.btnTimePicker).setOnClickListener {
            pickTime()
        }

        btnSave.setOnClickListener {
            saveSchedule()
        }
    }

    private fun pickDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            tvDate.text = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
        }, year, month, day).show()
    }

    private fun pickTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }
            tvTime.text = String.format("%02d:%02d", selectedHour, selectedMinute)
        }, hour, minute, true).show()
    }

    private fun saveSchedule() {
        val kelas = etKelas.text.toString()
        val mapel = etMapel.text.toString()
        val repeatWeekly = cbRepeatWeekly.isChecked

        createClassViewModel.saveSchedule(kelas, mapel, selectedDate, selectedTime, repeatWeekly, emptyList(), emptyList()) { success, message ->
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
            startActivity(Intent(this, TabLayoutActivity::class.java))
        }
    }
}