package com.example.preptalk.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val role: String,
    val difficulty: String,
    val date: String,
    val score: Int,
    val feedbackJson: String   // stores question/answer/feedback list as JSON
)