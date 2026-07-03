package com.example.preptalk.model

data class Message(
    val content: String,
    val role: MessageRole,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageRole {
    USER, AI, TYPING  // TYPING is for the "..." bubble while AI is responding
}