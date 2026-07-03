package com.example.preptalk.network

import com.example.preptalk.model.ApiRequest
import com.example.preptalk.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GeminiApiService {

    @POST("chat/completions")
    suspend fun generateContent(
        @Header("Authorization") apiKey: String,
        @Body request: ApiRequest
    ): Response<ApiResponse>
}