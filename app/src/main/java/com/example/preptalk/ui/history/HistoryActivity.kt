package com.example.preptalk.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preptalk.R
import com.example.preptalk.databinding.ActivityHistoryBinding
import com.example.preptalk.repository.SessionRepository
import com.example.preptalk.ui.home.HomeActivity
import com.example.preptalk.ui.summary.SummaryActivity
import com.example.preptalk.ui.settings.SettingsActivity
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var sessionRepository: SessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionRepository = SessionRepository(applicationContext)

        loadSessions()
        setupStartButton()
        setupBottomNav()
    }

    override fun onResume() {
        super.onResume()
        loadSessions()
    }

    private fun loadSessions() {
        lifecycleScope.launch {
            val sessionEntities = sessionRepository.getAllSessions()

            val sessions = sessionEntities.map { entity ->
                SessionSummary(
                    id         = entity.id,
                    role       = entity.role,
                    difficulty = entity.difficulty,
                    date       = entity.date,
                    score      = entity.score
                )
            }

            if (sessions.isEmpty()) {
                binding.layoutEmptyState.visibility = View.VISIBLE
                binding.rvHistory.visibility        = View.GONE
            } else {
                binding.layoutEmptyState.visibility = View.GONE
                binding.rvHistory.visibility        = View.VISIBLE
                binding.rvHistory.layoutManager     = LinearLayoutManager(this@HistoryActivity)
                binding.rvHistory.adapter           = HistoryAdapter(sessions) { clickedSession ->
                    openSessionDetail(clickedSession.id)
                }
            }
        }
    }

    private fun openSessionDetail(sessionId: Long) {
        val intent = Intent(this, SummaryActivity::class.java)
        intent.putExtra("SESSION_ID", sessionId)
        intent.putExtra("VIEW_ONLY", true)
        startActivity(intent)
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
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}