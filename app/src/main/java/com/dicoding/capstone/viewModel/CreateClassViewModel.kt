package com.dicoding.capstone.ui.classroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.UUID

class CreateClassViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun saveSchedule(
        kelas: String,
        mapel: String,
        selectedDate: Calendar?,
        selectedTime: Calendar?,
        repeatWeekly: Boolean,
        emailParticipants: List<String>,
        nameParticipants: List<String>,
        callback: (Boolean, String) -> Unit
    ) {
        if (kelas.isEmpty() || mapel.isEmpty() || selectedDate == null || selectedTime == null) {
            callback(false, "Please fill all the fields")
            return
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userName = currentUser.displayName ?: "Unknown"
            val teacherEmail = currentUser.email ?: "Unknown"
            val classCode = UUID.randomUUID().toString().substring(0, 8) // Generate a unique class code

            val classData = hashMapOf(
                "kelas" to kelas,
                "mapel" to mapel,
                "date" to selectedDate.time,
                "time" to selectedTime.time,
                "repeatWeekly" to repeatWeekly,
                "teacherName" to userName,
                "teacherEmail" to teacherEmail,
                "classCode" to classCode,
                "emailParticipants" to emailParticipants,
                "nameParticipants" to nameParticipants,

            )

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    db.collection("classes")
                        .add(classData)
                        .await()
                    callback(true, "Class created successfully with code: $classCode")
                } catch (e: Exception) {
                    callback(false, "Failed to create class: ${e.message}")
                }
            }
        } else {
            callback(false, "User not authenticated")
        }
    }
}
