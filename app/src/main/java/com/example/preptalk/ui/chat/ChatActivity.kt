package com.example.preptalk.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preptalk.databinding.ActivityChatBinding
import com.example.preptalk.model.Message
import com.example.preptalk.model.MessageRole
import com.example.preptalk.ui.summary.SummaryActivity
import com.example.preptalk.viewmodel.ChatViewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val viewModel: ChatViewModel by viewModels()

    private var role       = "Android"
    private var difficulty = "Mid-level"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        role       = intent.getStringExtra("ROLE")       ?: "Android"
        difficulty = intent.getStringExtra("DIFFICULTY") ?: "Mid-level"

        binding.tvToolbarTitle.text = "$role Interview"

        setupRecyclerView()
        setupSendButton()
        setupHint()
        setupBackButton()
        setupEndSession()
        observeViewModel()

        // Kick off the interview
        viewModel.init(role, difficulty)
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.rvChat.apply {
            adapter       = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
        }
    }

    private fun observeViewModel() {
        // Observe messages list
        viewModel.messages.observe(this) { messages ->
            chatAdapter.setMessages(messages)
            if (messages.isNotEmpty()) scrollToBottom()
        }

        // Observe loading — show/hide typing bubble
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                chatAdapter.showTyping()
                scrollToBottom()
            } else {
                chatAdapter.hideTyping()
            }
            binding.btnSend.isEnabled  = !isLoading
            binding.etAnswer.isEnabled = !isLoading
        }

        // Observe session complete
        viewModel.sessionComplete.observe(this) { score ->
            score?.let {
                val intent = Intent(this, SummaryActivity::class.java)
                intent.putExtra("ROLE",       role)
                intent.putExtra("DIFFICULTY", difficulty)
                intent.putExtra("SCORE",      it)
                startActivity(intent)
                finish()
            }
        }

        // Observe errors
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSendButton() {
        binding.btnSend.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val text = binding.etAnswer.text?.toString()?.trim() ?: return
        if (text.isEmpty()) return
        binding.etAnswer.setText("")
        viewModel.sendUserMessage(text)
    }

    private fun setupHint() {
        binding.layoutHint.setOnClickListener {
            viewModel.sendUserMessage("Can you give me a hint?")
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupEndSession() {
        binding.tvEndSession.setOnClickListener {
            val intent = Intent(this, SummaryActivity::class.java)
            intent.putExtra("ROLE",       role)
            intent.putExtra("DIFFICULTY", difficulty)
            intent.putExtra("SCORE",      50)
            startActivity(intent)
            finish()
        }
    }

    private fun scrollToBottom() {
        binding.rvChat.post {
            binding.rvChat.smoothScrollToPosition(chatAdapter.itemCount - 1)
        }
    }
}