package com.dicoding.capstone.ui.tabLayout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.capstone.R
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.databinding.ActivityHomepageBinding
import com.dicoding.capstone.ui.classroom.CreateClassActivity
import com.dicoding.capstone.ui.classroom.JoinClassActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_tab_layout)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val btnAdd: FloatingActionButton = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            showAddClassDialog()
        }
    }

    private fun showAddClassDialog() {
        val userPreference = UserPreference(this)
        val role = userPreference.getUserRole() // Mengambil role dari UserPreference

        // Cek apakah role adalah teacher
        val options = if (role == "teacher") {
            arrayOf("Gabung ke Kelas", "Buat Kelas Baru")
        } else {
            arrayOf("Gabung ke Kelas")
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Tentukan Kelas")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(this, JoinClassActivity::class.java)
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(this, CreateClassActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            .show()
    }
}

