package com.example.preptalk.ui.summary

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.preptalk.R
import com.example.preptalk.databinding.ActivitySummaryBinding
import com.example.preptalk.ui.home.HomeActivity

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val role       = intent.getStringExtra("ROLE")       ?: "Android"
        val difficulty = intent.getStringExtra("DIFFICULTY") ?: "Mid-level"
        val score      = intent.getIntExtra("SCORE", 72)

        setupHeader(role, difficulty, score)
        setupFeedbackCards()
        setupButtons(role, difficulty)
    }

    private fun setupHeader(role: String, difficulty: String, score: Int) {
        binding.tvRoleDifficulty.text = "$role • $difficulty"
        binding.tvScore.text = "$score%"
        binding.progressScore.progress = score
    }

    private fun setupFeedbackCards() {
        // Placeholder feedback — will be replaced with real AI feedback after LLM integration
        val feedbackItems = listOf(
            Triple(
                "Describe the Activity Lifecycle and how you manage state during a configuration change.",
                "I usually use onSaveInstanceState to store primitives and ViewModel to handle more complex data during rotations...",
                "Good technical knowledge. You could improve by mentioning the specific nuances of ViewModel persistence versus onSaveInstanceState size limits."
            ),
            Triple(
                "What are the advantages of using Coroutines over traditional Threads for network calls?",
                "Coroutines are lightweight and allow for non-blocking asynchronous code that is easier to read than callbacks.",
                "Strong explanation of structured concurrency. Consider discussing the Main dispatcher and context switching for a more senior-level response."
            ),
            Triple(
                "Tell me about a time you had a conflict with a designer over a technical constraint.",
                "I explained why the custom animation would hit the frame rate and we found a simpler middle ground together.",
                "Effective communication strategy. Using the STAR method would make this answer even stronger."
            )
        )

        feedbackItems.forEach { (question, answer, feedback) ->
            val cardView = LayoutInflater.from(this)
                .inflate(R.layout.item_feedback_card, binding.layoutFeedbackCards, false)

            cardView.findViewById<TextView>(R.id.tvQuestion).text   = question
            cardView.findViewById<TextView>(R.id.tvUserAnswer).text = "\"$answer\""
            cardView.findViewById<TextView>(R.id.tvAiFeedback).text = feedback

            binding.layoutFeedbackCards.addView(cardView)
        }
    }

    private fun setupButtons(role: String, difficulty: String) {
        binding.btnSaveSession.setOnClickListener {
            // Will save to Room DB after DB integration
        }

        binding.btnTryAgain.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        binding.btnClose.setOnClickListener { finish() }
    }
}