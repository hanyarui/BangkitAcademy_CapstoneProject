package com.dicoding.capstone.data

data class ClassDetails(
    val className: String,
    val subject: String,
    val classCode: String,
    val teacherUsername: String,
    val students: List<String>
)
