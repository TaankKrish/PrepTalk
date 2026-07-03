package com.example.preptalk.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.preptalk.R
import com.example.preptalk.model.Message
import com.example.preptalk.model.MessageRole

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<Message>()
    private var isTyping = false

    companion object {
        const val VIEW_TYPE_AI     = 0
        const val VIEW_TYPE_USER   = 1
        const val VIEW_TYPE_TYPING = 2
    }

    inner class AiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tvAiMessage)
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tvUserMessage)
    }

    inner class TypingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tvAiMessage)
    }

    override fun getItemViewType(position: Int): Int {
        if (isTyping && position == itemCount - 1) return VIEW_TYPE_TYPING
        return when (messages[position].role) {
            MessageRole.USER -> VIEW_TYPE_USER
            else             -> VIEW_TYPE_AI
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER -> UserViewHolder(
                inflater.inflate(R.layout.item_message_user, parent, false)
            )
            else -> AiViewHolder(
                inflater.inflate(R.layout.item_message_ai, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AiViewHolder   -> {
                if (isTyping && position == itemCount - 1) {
                    holder.tvMessage.text = "..."
                } else {
                    holder.tvMessage.text = messages[position].content
                }
            }
            is UserViewHolder -> holder.tvMessage.text = messages[position].content
        }
    }

    override fun getItemCount() = if (isTyping) messages.size + 1 else messages.size

    fun setMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    fun showTyping() {
        isTyping = true
        notifyItemInserted(itemCount - 1)
    }

    fun hideTyping() {
        isTyping = false
        notifyDataSetChanged()
    }
}