package com.example.preptalk.ui.history

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preptalk.databinding.ActivityHistoryBinding
import com.example.preptalk.ui.home.HomeActivity
import android.widget.Toast
import com.example.preptalk.R

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSessions()
        setupStartButton()
        setupBottomNav()
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

    private fun setupBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_history
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_history -> true
                R.id.nav_interviews -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("HIGHLIGHT_ROLES", true)
                    startActivity(intent)
                    Toast.makeText(this, "Please select a role to start", Toast.LENGTH_SHORT).show()
                    false
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show()
                    false
                }
                else -> false
            }
        }
    }
}