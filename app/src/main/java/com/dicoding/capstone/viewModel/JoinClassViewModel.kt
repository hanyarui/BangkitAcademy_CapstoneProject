package com.dicoding.capstone.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.data.JoinClassRequest
import com.dicoding.capstone.data.JoinClassResponse
import com.dicoding.capstone.data.StudentRequest
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.data.service.ApiConfig
import com.dicoding.capstone.data.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class JoinClassViewModel(private val api: ApiConfig, private val userPreference: UserPreference) : ViewModel() {

    private val _joinClassResult = MutableLiveData<JoinClassResponse>()
    val joinClassResult: LiveData<JoinClassResponse> = _joinClassResult

    fun joinClass(classCode: String) {
        viewModelScope.launch {
            try {
                val studentId = userPreference.getUserName()
                if (studentId != null) {
                    val request = JoinClassRequest(studentId)
                    val response = api.joinClass(classCode, request)
                    if (response.isSuccessful) {
                        _joinClassResult.postValue(response.body())
                    } else {
                        _joinClassResult.postValue(JoinClassResponse(false, "Failed to join class"))
                    }
                } else {
                    _joinClassResult.postValue(JoinClassResponse(false, "Invalid student ID"))
                }
            } catch (e: Exception) {
                _joinClassResult.postValue(JoinClassResponse(false, "Error: ${e.message}"))
            }
        }
    }
}


