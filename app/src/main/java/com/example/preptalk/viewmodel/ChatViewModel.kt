package com.example.preptalk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preptalk.model.ApiMessage
import com.example.preptalk.model.Message
import com.example.preptalk.model.MessageRole
import com.example.preptalk.repository.ChatRepository
import com.example.preptalk.utils.PromptBuilder
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    // Messages shown in the chat UI
    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages

    // Loading state — true when waiting for AI response
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // Fired when session is complete with the score
    private val _sessionComplete = MutableLiveData<Int?>()
    val sessionComplete: LiveData<Int?> = _sessionComplete

    // Error state
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // Conversation history sent to Anthropic (alternating user/assistant turns)
    private val conversationHistory = mutableListOf<ApiMessage>()

    private var role       = "Android"
    private var difficulty = "Mid-level"

    fun init(role: String, difficulty: String) {
        this.role       = role
        this.difficulty = difficulty
        sendToAi(isFirstMessage = true)
    }

    fun sendUserMessage(userInput: String) {
        // Add user message to UI
        addMessage(Message(content = userInput, role = MessageRole.USER))

        // Add to conversation history for API
        conversationHistory.add(
            ApiMessage(role = "user", content = userInput)
        )

        sendToAi()
    }

    private fun sendToAi(isFirstMessage: Boolean = false) {
        _isLoading.value = true

        // For first message, send a trigger to get the first question
        if (isFirstMessage) {
            conversationHistory.add(
                ApiMessage(role = "user", content = "Start the interview. Ask me the first question.")
            )
        }

        viewModelScope.launch {
            val result = repository.sendMessage(
                role                = role,
                difficulty          = difficulty,
                conversationHistory = conversationHistory
            )

            result.onSuccess { responseText ->
                // Add AI response to conversation history
                conversationHistory.add(
                    ApiMessage(role = "assistant", content = responseText)
                )

                // Check if session is complete
                if (PromptBuilder.isSessionComplete(responseText)) {
                    val score = PromptBuilder.extractScore(responseText)
                    _sessionComplete.value = score
                } else {
                    addMessage(Message(content = responseText, role = MessageRole.AI))
                }
            }

            result.onFailure { error ->
                _error.value = "Connection error: ${error.message}"
                addMessage(
                    Message(
                        content = "Sorry, I'm having trouble connecting. Please check your internet and try again.",
                        role    = MessageRole.AI
                    )
                )
            }

            _isLoading.value = false
        }
    }

    private fun addMessage(message: Message) {
        val current = _messages.value.orEmpty().toMutableList()
        current.add(message)
        _messages.value = current
    }

    fun getConversationForSummary(): List<Message> {
        return _messages.value.orEmpty()
    }
}