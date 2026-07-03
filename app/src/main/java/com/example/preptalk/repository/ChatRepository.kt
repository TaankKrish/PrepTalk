package com.example.preptalk.repository

import android.util.Log
import com.example.preptalk.model.ApiMessage
import com.example.preptalk.model.ApiRequest
import com.example.preptalk.network.RetrofitClient
import com.example.preptalk.utils.Constants
import com.example.preptalk.utils.PromptBuilder

class ChatRepository {

    private val api = RetrofitClient.geminiApiService

    suspend fun sendMessage(
        role: String,
        difficulty: String,
        conversationHistory: List<ApiMessage>
    ): Result<String> {
        return try {
            val messages = mutableListOf<ApiMessage>()
            messages.add(
                ApiMessage(
                    role    = "system",
                    content = PromptBuilder.buildSystemPrompt(role, difficulty)
                )
            )
            messages.addAll(conversationHistory)

            val request = ApiRequest(
                model     = Constants.MODEL_NAME,
                messages  = messages,
                maxTokens = Constants.MAX_TOKENS
            )

            Log.d("ChatRepo", "Calling model: ${Constants.MODEL_NAME}")

            val response = api.generateContent(
                apiKey  = "Bearer ${Constants.GEMINI_API_KEY}",
                request = request
            )

            Log.d("ChatRepo", "Response code: ${response.code()}")

            if (response.isSuccessful) {
                val text = response.body()
                    ?.choices
                    ?.firstOrNull()
                    ?.message
                    ?.content
                    ?: "Sorry, I couldn't generate a response."
                Result.success(text)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("ChatRepo", "Error code: ${response.code()} ${response.message()}")
                Log.e("ChatRepo", "Error body: $errorBody")
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("ChatRepo", "Exception: ${e.message}")
            Result.failure(e)
        }
    }
}