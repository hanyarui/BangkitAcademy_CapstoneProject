package com.dicoding.capstone.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.data.theme.SettingPreference
import com.dicoding.capstone.ui.auth.AuthViewModel
import com.dicoding.capstone.ui.classroom.CreateClassViewModel
import com.dicoding.capstone.ui.tabLayout.ui.profile.ProfileViewModel
import com.dicoding.capstone.viewModel.ClassListViewModel
import com.dicoding.capstone.viewModel.JoinClassViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val auth: FirebaseAuth,
    private val userPreference: UserPreference? = null,
    private val settingPreference: SettingPreference? = null,
    private val db: FirebaseFirestore? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(auth, userPreference ?: throw IllegalArgumentException("UserPreference is required")) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(settingPreference ?: throw IllegalArgumentException("SettingPreference is required")) as T
            }
            modelClass.isAssignableFrom(CreateClassViewModel::class.java) -> {
                CreateClassViewModel() as T
            }
            modelClass.isAssignableFrom(JoinClassViewModel::class.java) -> {
                JoinClassViewModel(auth, db ?: throw IllegalArgumentException("Firestore database is required")) as T
            }
//            modelClass.isAssignableFrom(ClassListViewModel::class.java) -> {
//                ClassListViewModel(auth, db ?: throw IllegalArgumentException("Firestore database is required")) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
