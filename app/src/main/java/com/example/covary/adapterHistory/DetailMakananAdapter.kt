package com.example.covary.adapterHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covary.R

class DetailMakananAdapter(private val makananList: List<MakananDetail>) :
    RecyclerView.Adapter<DetailMakananAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaDM)
        val tvjenisMakanan: TextView = itemView.findViewById(R.id.tvJenisDetailMakanan)
        val tvKalori: TextView = itemView.findViewById(R.id.tvKaloriDM)
        val tvKarbo: TextView = itemView.findViewById(R.id.tvKarboDM)
        val tvLemak: TextView = itemView.findViewById(R.id.tvLemakDM)
        val tvProtein: TextView = itemView.findViewById(R.id.tvProteinDM)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_makanan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = makananList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = makananList[position]
        holder.tvNama.text = "${item.nama} (${item.gram} g)"
        holder.tvjenisMakanan.text = "${item.jenisMakanan}"
        holder.tvKalori.text = "Kalori: ${item.kalori}g"
        holder.tvKarbo.text = "Karbohidrat: ${item.karbohidrat}g"
        holder.tvLemak.text = "Lemak: ${item.lemak}g"
        holder.tvProtein.text = "Protein: ${item.protein}g"
    }
}