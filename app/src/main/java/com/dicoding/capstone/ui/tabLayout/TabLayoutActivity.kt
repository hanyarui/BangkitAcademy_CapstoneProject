package com.dicoding.capstone.ui.tabLayout

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.ActivityTabLayoutBinding
import com.dicoding.capstone.ui.auth.LoginActivity
import com.dicoding.capstone.ui.classroom.CreateClassActivity
import com.dicoding.capstone.ui.classroom.JoinClassActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TabLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTabLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTabLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_tab_layout)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_add,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        navView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_add -> {
//                    showAddClassDialog()
//                    true // Menandakan bahwa item telah ditangani
//                }
//                else -> false // Biarkan Navigation Component menangani item lainnya
//            }
//        }

        val btnAdd: FloatingActionButton = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            showAddClassDialog()
        }
    }

    private fun showAddClassDialog() {
        val options = arrayOf("Gabung ke Kelas", "Buat Kelas Baru")
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