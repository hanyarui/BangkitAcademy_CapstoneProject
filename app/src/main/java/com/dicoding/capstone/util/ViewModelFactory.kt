package com.dicoding.capstone.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.viewModel.AuthViewModel
import android.content.Context
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.data.service.ApiConfig
import com.dicoding.capstone.viewModel.ClassViewModel
//import com.dicoding.capstone.viewModel.ClassViewModel
import com.dicoding.capstone.viewModel.JoinClassViewModel

class ViewModelFactory(
    private val context: Context? = null,
    private val userPreference: UserPreference? = null,
    private val apiService: ApiConfig? = null,
    private val application: Application? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                // Pastikan context tidak null saat membuat AuthViewModel
                AuthViewModel(context!!) as T
            }
            modelClass.isAssignableFrom(ClassViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return ClassViewModel(application!!) as T
            }
            modelClass.isAssignableFrom(JoinClassViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return JoinClassViewModel(apiService!!, userPreference!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

