package com.dicoding.capstone.data

data class JoinClassRequest(
    val studentId: String
)

data class JoinClassResponse(
    val success: Boolean,
    val message: String
)
