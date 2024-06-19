package com.dicoding.capstone.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.data.local.UserPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val auth: FirebaseAuth,
    private val userPreference: UserPreference
) : ViewModel() {

    fun registerUser(name: String, email: String, password: String, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Register user with Firebase Auth
                auth.createUserWithEmailAndPassword(email, password).await()

                // Update user profile with displayName
                val user = auth.currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                user?.updateProfile(profileUpdates)?.await()

                // Save user data to SharedPreferences
                userPreference.saveUser(name, email, "")

                callback(true, null)
            } catch (e: Exception) {
                callback(false, e.message)
            }
        }
    }

    fun loginUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                auth.signInWithEmailAndPassword(email, password).await()

                // Retrieve user data from Firebase Auth
                val user = auth.currentUser
                val name = user?.displayName ?: "Unknown"

                // Save user data to SharedPreferences
                userPreference.saveUser(name, email, "")

                callback(true, null)
            } catch (e: Exception) {
                callback(false, e.message)
            }
        }
    }

    fun getUser(): Map<String, String?> {
        return userPreference.getUser()
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            auth.signOut()
            userPreference.clearUser()
        }
    }
}
