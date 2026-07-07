package com.example.preptalk.ui.summary

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.preptalk.R
import com.example.preptalk.databinding.ActivitySummaryBinding
import com.example.preptalk.repository.SessionRepository
import com.example.preptalk.ui.home.HomeActivity
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FeedbackItem(
    val question: String,
    val answer: String,
    val feedback: String
)

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding
    private lateinit var sessionRepository: SessionRepository

    private var role         = "Android"
    private var difficulty   = "Mid-level"
    private var score        = 50
    private var feedbackJson = "{}"
    private var isViewOnly   = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionRepository = SessionRepository(applicationContext)

        isViewOnly = intent.getBooleanExtra("VIEW_ONLY", false)

        if (isViewOnly) {
            loadSessionFromDb()
        } else {
            role         = intent.getStringExtra("ROLE")       ?: "Android"
            difficulty   = intent.getStringExtra("DIFFICULTY") ?: "Mid-level"
            score        = intent.getIntExtra("SCORE", 50)
            feedbackJson = intent.getStringExtra("FEEDBACK_JSON") ?: "{}"

            setupHeader(role, difficulty, score)
            setupFeedbackCards(feedbackJson)
            setupButtons()
        }
    }

    private fun loadSessionFromDb() {
        val sessionId = intent.getLongExtra("SESSION_ID", -1L)

        lifecycleScope.launch {
            val session = sessionRepository.getSessionById(sessionId)

            if (session != null) {
                role         = session.role
                difficulty   = session.difficulty
                score        = session.score
                feedbackJson = session.feedbackJson

                setupHeader(role, difficulty, score)
                setupFeedbackCards(feedbackJson)
                setupButtons()
            } else {
                Toast.makeText(this@SummaryActivity, "Session not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupHeader(role: String, difficulty: String, score: Int) {
        binding.tvRoleDifficulty.text = "$role • $difficulty"
        binding.tvScore.text = "$score%"
        binding.progressScore.progress = score
    }

    private fun setupFeedbackCards(feedbackJsonRaw: String) {
        val feedbackItems: List<FeedbackItem> = try {
            val jsonObject = JsonParser.parseString(feedbackJsonRaw).asJsonObject
            val feedbackArray = jsonObject.getAsJsonArray("feedback")
            Gson().fromJson(feedbackArray, Array<FeedbackItem>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }

        if (feedbackItems.isEmpty()) {
            binding.layoutFeedbackCards.addView(
                TextView(this).apply {
                    text = "No detailed feedback available for this session."
                    setTextColor(getColor(R.color.text_secondary))
                    setPadding(0, 20, 0, 20)
                }
            )
            return
        }

        feedbackItems.forEach { item ->
            val cardView = LayoutInflater.from(this)
                .inflate(R.layout.item_feedback_card, binding.layoutFeedbackCards, false)

            cardView.findViewById<TextView>(R.id.tvQuestion).text   = item.question
            cardView.findViewById<TextView>(R.id.tvUserAnswer).text = "\"${item.answer}\""
            cardView.findViewById<TextView>(R.id.tvAiFeedback).text = item.feedback

            binding.layoutFeedbackCards.addView(cardView)
        }
    }

    private fun setupButtons() {
        if (isViewOnly) {
            // Already saved — hide Save button, repurpose Try Again as "Back"
            binding.btnSaveSession.visibility = View.GONE
            binding.btnTryAgain.text = "Back to History"
            binding.btnTryAgain.setOnClickListener { finish() }
        } else {
            binding.btnSaveSession.setOnClickListener {
                saveSessionToDb()
            }
            binding.btnTryAgain.setOnClickListener {
                goToHome()
            }
        }

        binding.btnClose.setOnClickListener { finish() }
    }

    private fun saveSessionToDb() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        lifecycleScope.launch {
            sessionRepository.saveSession(
                role         = role,
                difficulty   = difficulty,
                date         = currentDate,
                score        = score,
                feedbackJson = feedbackJson
            )

            Toast.makeText(this@SummaryActivity, "Session saved!", Toast.LENGTH_SHORT).show()
            goToHome()
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}