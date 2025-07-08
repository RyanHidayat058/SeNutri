package com.example.covary.slider

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covary.R

class SliderAdapter(
    private val items: List<SliderItem>,
    private val listener: OnSliderItemClickListener
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    interface OnSliderItemClickListener {
        fun onSliderItemClicked(data: Bundle)
    }

    inner class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imageSlide)
        val title: TextView = view.findViewById(R.id.titleSlide)
        val desc: TextView = view.findViewById(R.id.descSlide)

        fun bind(item: SliderItem) {
            img.setImageResource(item.imageResId)
            title.text = item.title
            desc.text = item.description
            desc.maxLines = 5
            desc.ellipsize = TextUtils.TruncateAt.END

            itemView.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("title", item.title)
                    putString("description", item.description)
                    putInt("imageResId", item.imageResId)
                }
                listener.onSliderItemClicked(bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slide, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}