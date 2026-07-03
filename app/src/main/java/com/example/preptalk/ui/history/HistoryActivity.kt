package com.example.preptalk.ui.history

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preptalk.databinding.ActivityHistoryBinding
import com.example.preptalk.ui.home.HomeActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSessions()
        setupStartButton()
    }

    private fun loadSessions() {
        // Placeholder data — will be replaced with Room DB after integration
        val sessions = listOf(
            SessionSummary("Android", "Mid",    "Oct 24, 2023", 85),
            SessionSummary("Frontend", "Senior", "Oct 20, 2023", 92),
            SessionSummary("Backend",  "Fresher","Oct 15, 2023", 64)
        )

        if (sessions.isEmpty()) {
            binding.layoutEmptyState.visibility = android.view.View.VISIBLE
            binding.rvHistory.visibility        = android.view.View.GONE
        } else {
            binding.layoutEmptyState.visibility = android.view.View.GONE
            binding.rvHistory.visibility        = android.view.View.VISIBLE
            binding.rvHistory.layoutManager     = LinearLayoutManager(this)
            binding.rvHistory.adapter           = HistoryAdapter(sessions)
        }
    }

    private fun setupStartButton() {
        binding.btnStartInterview.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}