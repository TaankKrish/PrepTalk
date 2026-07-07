package com.example.preptalk.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.example.preptalk.R

data class SessionSummary(
    val id: Long,
    val role: String,
    val difficulty: String,
    val date: String,
    val score: Int
)

class HistoryAdapter(
    private val sessions: List<SessionSummary>,
    private val onItemClick: (SessionSummary) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRole: TextView       = view.findViewById(R.id.tvRole)
        val tvDifficulty: TextView = view.findViewById(R.id.tvDifficulty)
        val tvDate: TextView       = view.findViewById(R.id.tvDate)
        val tvScore: TextView      = view.findViewById(R.id.tvScore)
        val progressScore: CircularProgressIndicator = view.findViewById(R.id.progressScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_session_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val session = sessions[position]
        holder.tvRole.text       = session.role
        holder.tvDifficulty.text = session.difficulty
        holder.tvDate.text       = session.date
        holder.tvScore.text      = "${session.score}%"
        holder.progressScore.progress = session.score

        val color = when {
            session.score >= 80 -> 0xFF00BCD4.toInt() // teal
            session.score >= 60 -> 0xFFBB86FC.toInt() // purple
            else                -> 0xFFEF5350.toInt() // red
        }
        holder.progressScore.setIndicatorColor(color)
        holder.itemView.findViewById<View>(R.id.viewAccent)
            .setBackgroundColor(color)

        holder.itemView.setOnClickListener {
            onItemClick(session)
        }
    }

    override fun getItemCount() = sessions.size
}