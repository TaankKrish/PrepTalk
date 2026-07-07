package com.example.preptalk.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.preptalk.BuildConfig
import com.example.preptalk.R
import com.example.preptalk.databinding.ActivitySettingsBinding
import com.example.preptalk.repository.SessionRepository
import com.example.preptalk.ui.history.HistoryActivity
import com.example.preptalk.ui.home.HomeActivity
import android.widget.Toast
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sessionRepository: SessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionRepository = SessionRepository(applicationContext)

        setupVersion()
        setupClearHistory()
        setupBottomNav()
    }

    private fun setupVersion() {
        binding.tvVersion.text = "Version ${BuildConfig.VERSION_NAME}"
    }

    private fun setupClearHistory() {
        binding.cardClearHistory.setOnClickListener {
            showClearHistoryConfirmation()
        }
    }

    private fun showClearHistoryConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Clear All History?")
            .setMessage("This will permanently delete all your saved interview sessions. This action cannot be undone.")
            .setPositiveButton("Clear") { _, _ ->
                clearHistory()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun clearHistory() {
        lifecycleScope.launch {
            sessionRepository.clearAllSessions()
            Toast.makeText(this@SettingsActivity, "History cleared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_settings
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.nav_interviews -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("HIGHLIGHT_ROLES", true)
                    startActivity(intent)
                    Toast.makeText(this, "Please select a role to start", Toast.LENGTH_SHORT).show()
                    false
                }
                R.id.nav_settings -> true
                else -> false
            }
        }
    }
}