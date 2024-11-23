package com.example.data.response

data class SimpleResponse(
    val success: Boolean,
    val message: String,
    val statusCode: Int? = null
)