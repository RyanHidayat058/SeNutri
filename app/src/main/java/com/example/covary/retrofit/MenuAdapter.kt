package com.example.covary.retrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covary.R

class MenuAdapter(private val originalList: List<MenuItem>, private val listener: OnItemClickListener) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private var filteredList: MutableList<MenuItem> = originalList.toMutableList()

    interface OnItemClickListener {
        fun onAddClicked(item: MenuItem)
    }

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCalories: TextView = view.findViewById(R.id.tvCalories)
        val tvProtein: TextView = view.findViewById(R.id.tvProtein)
        val tvFat: TextView = view.findViewById(R.id.tvFat)
        val tvCarbo: TextView = view.findViewById(R.id.tvCarbo)
        val btnAdd: View = view.findViewById(R.id.btnAdd) // pastikan ini ada di layout XML item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = filteredList[position]
        holder.tvName.text = "${menu.nama} ${menu.gram}"
        holder.tvCalories.text = "Kalori: ${menu.kalori}"
        holder.tvProtein.text = "Protein: ${menu.protein}"
        holder.tvFat.text = "Lemak: ${menu.lemak}"
        holder.tvCarbo.text = "Karbohidrat: ${menu.karbohidrat}"

        holder.btnAdd.setOnClickListener {
            listener.onAddClicked(menu)
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun filter(query: String) {
        val lowerQuery = query.lowercase().trim()
        filteredList = if (lowerQuery.isEmpty()) {
            originalList.toMutableList()
        } else {
            originalList.filter {
                it.nama.lowercase().contains(lowerQuery)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}