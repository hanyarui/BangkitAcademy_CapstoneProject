package com.dicoding.capstone.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.data.response.LoginResponse
import com.dicoding.capstone.data.response.PredictionResponse
import com.dicoding.capstone.data.response.RegisterResponse
import com.dicoding.capstone.data.service.ApiService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AuthViewModel(private val context: Context?) : ViewModel() {

    private val userPreference = UserPreference(context)

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    fun register(username: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            ApiService.instance.register(username, email, role, password).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        _registerResult.postValue(Result.success(response.body()) as Result<RegisterResponse>?)
                        Log.d("Register", "Success: ${response.body()}")
                    } else {
                        _registerResult.postValue(Result.failure(Exception("Registration failed: ${response.errorBody()}")))
                        Log.d("Register", "Failed: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _registerResult.postValue(Result.failure(t))
                    Log.e("Register", "Error: ${t.message}")
                }
            })
        }
    }

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            ApiService.instance.loginUser(username, password).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        _loginResult.postValue(Result.success(loginResponse) as Result<LoginResponse>?)

                        // Simpan token menggunakan UserPreference
                        loginResponse?.token?.let { userPreference.setToken(it) }

                        Log.d("LoginUser", "Success: ${response.body()}")
                    } else {
                        _loginResult.postValue(Result.failure(Exception("Login failed: ${response.errorBody()}")))
                        Log.d("LoginUser", "Failed: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _loginResult.postValue(Result.failure(t))
                    Log.e("LoginUser", "Error: ${t.message}")
                }
            })
        }
    }

    fun loginAdmin(username: String, password: String) {
        viewModelScope.launch {
            ApiService.instance.loginAdmin(username, password).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        _loginResult.postValue(Result.success(loginResponse) as Result<LoginResponse>?)

                        // Simpan token menggunakan UserPreference
                        loginResponse?.token?.let { userPreference.setToken(it) }

                        Log.d("LoginAdmin", "Success: ${response.body()}")
                    } else {
                        _loginResult.postValue(Result.success(response.body()) as Result<LoginResponse>?)
                        Log.d("LoginAdmin", "Failed: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LoginAdmin", "Error: ${t.message}")
                }
            })
        }
    }

    fun predict(token: String, imageFile: File) {
        viewModelScope.launch {
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
            val body = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
            ApiService.instance.predict("Bearer $token", body).enqueue(object : Callback<PredictionResponse> {
                override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                    if (response.isSuccessful) {
                        Log.d("Predict", "Success: ${response.body()}")
                    } else {
                        Log.d("Predict", "Failed: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                    Log.e("Predict", "Error: ${t.message}")
                }
            })
        }
    }

    fun logout() {
        userPreference.logout()
    }
}