package com.example.preptalk.network

import com.example.preptalk.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val geminiApiService: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.GEMINI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}