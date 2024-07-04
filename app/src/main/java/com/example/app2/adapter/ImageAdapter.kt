package com.example.app2.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.app2.R

import com.bumptech.glide.Glide
import com.example.app2.databinding.ItemRvImageBinding
import com.example.app2.model.ImageViewItem

class ImageAdapter(
    private val data: ArrayList<ImageViewItem>, private val listener: (ImageViewItem) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private lateinit var binding: ItemRvImageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        binding = ItemRvImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(newList: List<ImageViewItem>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class ImageViewHolder(
        private var binding: ItemRvImageBinding,
        private val listener: (ImageViewItem) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageResponse: ImageViewItem) {
            binding.apply {

                ivTick.isSelected = imageResponse.isSelected == true

                Glide.with(root.context)
                    .load(imageResponse.item.qualityUrls.thumb)
                    .into(ivItem)

                root.setOnClickListener {
                    ivTick.isSelected = !ivTick.isSelected
                    listener(imageResponse)
                }
            }
        }
    }
}