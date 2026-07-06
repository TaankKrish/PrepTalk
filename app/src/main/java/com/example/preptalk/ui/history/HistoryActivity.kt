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
        loadSessions()  // Refresh whenever screen becomes visible
    }

    private fun loadSessions() {
        lifecycleScope.launch {
            val sessionEntities = sessionRepository.getAllSessions()

            val sessions = sessionEntities.map { entity ->
                SessionSummary(
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
                binding.rvHistory.adapter           = HistoryAdapter(sessions)
            }
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