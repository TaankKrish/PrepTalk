package com.example.preptalk.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.preptalk.R
import com.example.preptalk.databinding.ActivityHomeBinding
import com.example.preptalk.ui.chat.ChatActivity
import com.example.preptalk.ui.history.HistoryActivity
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var selectedDifficulty = "Mid-level"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDifficultyCards()
        setupStartButton()
        setupBottomNav()

        if (intent.getBooleanExtra("HIGHLIGHT_ROLES", false)) {
            highlightRoleSection()
        }
    }

    private fun highlightRoleSection() {
        val chipGroup = binding.chipGroupRoles
        val pulseAnimation = android.view.animation.AlphaAnimation(1f, 0.3f).apply {
            duration = 400
            repeatMode = android.view.animation.Animation.REVERSE
            repeatCount = 3
        }
        chipGroup.startAnimation(pulseAnimation)
    }

    private fun setupDifficultyCards() {
        val cards = mapOf(
            binding.cardFresher  to "Fresher",
            binding.cardMidLevel to "Mid-level",
            binding.cardSenior   to "Senior"
        )

        highlightCard(binding.cardMidLevel)

        cards.forEach { (card, difficulty) ->
            card.setOnClickListener {
                selectedDifficulty = difficulty
                cards.keys.forEach { c -> resetCard(c) }
                highlightCard(card)
            }
        }
    }

    private fun highlightCard(card: MaterialCardView) {
        card.setCardBackgroundColor(getColor(R.color.purple_surface))
        card.strokeColor = getColor(R.color.stroke_selected)
        card.strokeWidth = 4
    }

    private fun resetCard(card: MaterialCardView) {
        card.setCardBackgroundColor(getColor(R.color.bg_card))
        card.strokeColor = getColor(R.color.stroke_default)
        card.strokeWidth = 2
    }

    private fun getSelectedRole(): String {
        return when (binding.chipGroupRoles.checkedChipId) {
            R.id.chipFrontend    -> "Frontend"
            R.id.chipBackend     -> "Backend"
            R.id.chipAndroid     -> "Android"
            R.id.chipDataScience -> "Data Science"
            R.id.chipDevOps      -> "DevOps"
            else                 -> "Android"
        }
    }

    private fun setupStartButton() {
        binding.btnStartInterview.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("ROLE", getSelectedRole())
            intent.putExtra("DIFFICULTY", selectedDifficulty)
            startActivity(intent)
        }
    }

    private fun setupBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_home
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home      -> true
                R.id.nav_history   -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.nav_interviews -> {
                    val intent = Intent(this, ChatActivity::class.java)
                    intent.putExtra("ROLE", getSelectedRole())
                    intent.putExtra("DIFFICULTY", selectedDifficulty)
                    startActivity(intent)
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