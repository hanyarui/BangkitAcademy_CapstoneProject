package com.dicoding.capstone.api

import com.dicoding.capstone.data.PredictionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict")
    fun predictFace(
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Call<PredictionResponse>
}


