package com.dicoding.capstone.data

data class ClassRequest(
    val studentId: List<String>? = null,
    val teacherUsername: String,
    val subject: String,
    val className: String
)

