package com.example.preptalk.model

import com.google.gson.annotations.SerializedName

data class ApiRequest(
    val model: String,
    val messages: List<ApiMessage>,
    @SerializedName("max_tokens")
    val maxTokens: Int
)

data class ApiMessage(
    val role: String,
    val content: String
)