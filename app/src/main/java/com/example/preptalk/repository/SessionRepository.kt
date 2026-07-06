package com.example.preptalk.repository

import android.content.Context
import com.example.preptalk.db.AppDatabase
import com.example.preptalk.db.SessionEntity

class SessionRepository(context: Context) {

    private val sessionDao = AppDatabase.getDatabase(context).sessionDao()

    suspend fun saveSession(
        role: String,
        difficulty: String,
        date: String,
        score: Int,
        feedbackJson: String
    ) {
        val session = SessionEntity(
            role         = role,
            difficulty   = difficulty,
            date         = date,
            score        = score,
            feedbackJson = feedbackJson
        )
        sessionDao.insertSession(session)
    }

    suspend fun getAllSessions(): List<SessionEntity> {
        return sessionDao.getAllSessions()
    }

    suspend fun getSessionById(id: Long): SessionEntity? {
        return sessionDao.getSessionById(id)
    }
}