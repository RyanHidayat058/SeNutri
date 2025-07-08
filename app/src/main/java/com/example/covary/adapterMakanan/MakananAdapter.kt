package com.example.covary.adapterMakanan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covary.R

class MakananAdapter(private val list: List<Makanan>) :
    RecyclerView.Adapter<MakananAdapter.MakananViewHolder>() {

    inner class MakananViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvKalori: TextView = view.findViewById(R.id.tvKalori)
        val tvKarbo: TextView = view.findViewById(R.id.tvKarbo)
        val tvProtein: TextView = view.findViewById(R.id.tvProtein)
        val tvLemak: TextView = view.findViewById(R.id.tvLemak)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakananViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_makanan, parent, false)
        return MakananViewHolder(view)
    }

    override fun onBindViewHolder(holder: MakananViewHolder, position: Int) {
        val item = list[position]
        holder.tvNama.text = item.nama
        holder.tvKalori.text = "Kalori: ${item.kalori}g"
        holder.tvKarbo.text = "Karbohidrat: ${item.karbohidrat}g"
        holder.tvProtein.text = "Protein: ${item.protein}g"
        holder.tvLemak.text = "Lemak: ${item.lemak}g"
    }

    override fun getItemCount() = list.size
}