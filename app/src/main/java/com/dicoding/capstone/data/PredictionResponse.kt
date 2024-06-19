package com.dicoding.capstone.data

data class PredictionResponse(
    val success: Boolean,
    val message: String,
    val data: List<PredictionData>
)

data class PredictionData(
    val label: String,
    val confidence: Float
)

