package com.dicoding.capstone.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.firebase.Timestamp

@Parcelize
data class ClassModel(
    val kelas: String? = null,
    val mapel: String? = null,
    val time: Timestamp? = null,
    val teacherName: String? = null
) : Parcelable
