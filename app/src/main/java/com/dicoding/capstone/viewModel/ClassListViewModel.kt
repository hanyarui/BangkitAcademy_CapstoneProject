package com.dicoding.capstone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.data.model.ClassModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ClassListViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _classList = MutableLiveData<List<ClassModel>>()
    val classList: LiveData<List<ClassModel>> = _classList

    fun fetchClassList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentUser = auth.currentUser?.email
                if (currentUser != null) {
                    val snapshot = db.collection("classes")
                        .whereArrayContains("emailParticipants", currentUser)
                        .get()
                        .await()
                    val classItems = snapshot.toObjects(ClassModel::class.java)
                    _classList.postValue(classItems)

                    Log.d("classItems","get: $classItems")
                }
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("ClassListViewModel", "Error fetching class list: ${e.message}")
            }
        }
    }
}
