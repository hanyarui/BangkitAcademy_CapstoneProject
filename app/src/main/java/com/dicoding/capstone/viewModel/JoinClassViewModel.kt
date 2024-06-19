package com.dicoding.capstone.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class JoinClassViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    fun joinClass(classCode: String, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val classQuerySnapshot = db.collection("classes")
                    .whereEqualTo("classCode", classCode)
                    .get()
                    .await()

                if (classQuerySnapshot.isEmpty) {
                    callback(false, "Class not found")
                    return@launch
                }

                val classDocument = classQuerySnapshot.documents[0]
                val currentUser = auth.currentUser
                val userEmail = currentUser?.email ?: "Unknown"
                val userName = currentUser?.displayName ?: "Unknown"

                val classData = classDocument.data?.toMutableMap()
                val emailParticipants = classData?.get("emails") as? MutableList<String> ?: mutableListOf()
                val nameParticipants = classData?.get("participants") as? MutableList<String> ?: mutableListOf()

                if (currentUser != null) {
                    emailParticipants.add(userEmail)
                    classData!!["emailParticipants"] = emailParticipants

                    nameParticipants.add(userName)
                    classData["nameParticipants"] = nameParticipants

                    db.collection("classes").document(classDocument.id)
                        .set(classData)
                        .await()

                    callback(true, "Successfully joined the class")
                } else {
                    callback(false, "User not authenticated")
                }
            } catch (e: Exception) {
                callback(false, "Failed to join class: ${e.message}")
            }
        }
    }
}
