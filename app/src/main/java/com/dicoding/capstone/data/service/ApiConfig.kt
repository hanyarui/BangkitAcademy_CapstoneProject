package com.dicoding.capstone.data.service

import com.dicoding.capstone.data.ClassDetails
import com.dicoding.capstone.data.ClassRequest
import com.dicoding.capstone.data.JoinClassRequest
import com.dicoding.capstone.data.JoinClassResponse
import com.dicoding.capstone.data.StudentRequest
import com.dicoding.capstone.data.response.ClassResponse
import com.dicoding.capstone.data.response.PredictionResponse
import com.dicoding.capstone.data.response.LoginResponse
import com.dicoding.capstone.data.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.*

interface ApiConfig {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("role") role: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login/user")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("login/admin")
    fun loginAdmin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("classes")
    fun createClass(
        @Body classRequest: ClassRequest
    ): Call<ClassResponse>

    @POST("classes/{classCode}/students")
    suspend fun addStudent(
        @Path("classCode") classCode: String,
        @Body student: StudentRequest
    ): Response<Void>

    @POST("classes/{classCode}/join")
    suspend fun joinClass(
        @Path("classCode") classCode: String,
        @Body body: JoinClassRequest
    ): Response<JoinClassResponse>

    @GET("classes")
    suspend fun getClass(): List<ClassDetails>

    @Multipart
    @POST("predict")
    fun predict(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>
}


