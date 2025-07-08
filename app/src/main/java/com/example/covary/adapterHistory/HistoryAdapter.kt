package com.example.covary.adapterHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covary.R

class HistoryAdapter(
    private val historyList: List<HistoryItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tanggalText: TextView = itemView.findViewById(R.id.tvTanggal)

        init {
            itemView.setOnClickListener {
                onItemClick(historyList[adapterPosition].tanggal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tanggalText.text = historyList[position].tanggal
    }
}