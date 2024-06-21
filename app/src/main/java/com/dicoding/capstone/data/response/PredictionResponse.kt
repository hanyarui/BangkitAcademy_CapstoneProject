package com.dicoding.capstone.data.response

import com.google.firebase.Timestamp

data class PredictionResponse(
    val status: String,
    val result: List<PredictionResult>
)

data class PredictionResult(
    val probability: Double,
    val className: String
)


