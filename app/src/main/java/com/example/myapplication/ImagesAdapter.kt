package com.example.myapplication

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemImageBinding

class ImagesAdapter(
        private val maps:List<Bitmap>
)  : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(val binding: ItemImageBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return maps.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val bp = maps[position]
        holder.binding.image.setImageBitmap(bp)
    }

}