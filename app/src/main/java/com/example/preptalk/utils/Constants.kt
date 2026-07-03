package com.example.preptalk.utils

import com.example.preptalk.BuildConfig

object Constants {
    const val GEMINI_API_KEY  = BuildConfig.GROQ_API_KEY
    const val GEMINI_BASE_URL = "https://api.groq.com/openai/v1/"
    const val MODEL_NAME      = "llama-3.3-70b-versatile"
    const val MAX_TOKENS      = 1024
    const val MAX_QUESTIONS   = 5
}