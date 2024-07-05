package com.example.app2.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.app2.databinding.ItemLoadMoreBinding
import com.example.app2.databinding.ItemRvImageBinding
import com.example.app2.model.ImageViewItem
import com.example.app2.utils.Constants.VIEW_TYPE_ITEM
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_MORE

class ImageAdapter(
    private val data: ArrayList<ImageViewItem?>, private val listener: (ImageViewItem) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private lateinit var binding: ItemRvImageBinding
    private lateinit var bindingLoadMore: ItemLoadMoreBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        binding = ItemRvImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        bindingLoadMore =
            ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_LOAD_MORE) ImageViewHolder(bindingLoadMore, listener)
        else ImageViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] == null) VIEW_TYPE_LOAD_MORE else VIEW_TYPE_ITEM
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE) 2 else 1
            }
        }
    }

    fun addLoadMore() {
        if (data.last() == null) return
        data.add(null)
        notifyItemInserted(data.size - 1)
    }

    fun removeLoadMore() {
        if (data.size == 0) return
        if (data.last() != null) return
        data.removeAt(data.size - 1)
        notifyItemRemoved(data.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(newList: List<ImageViewItem>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        data[position]?.let { holder.bind(it) }
    }

    class ImageViewHolder(
        private var binding: ViewBinding, private val listener: (ImageViewItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageResponse: ImageViewItem) {
            if (binding is ItemRvImageBinding) {
                (binding as ItemRvImageBinding).apply {

                    ivTick.isSelected = imageResponse.isSelected == true

                    Glide.with(root.context).load(imageResponse.item.qualityUrls.thumb).into(ivItem)

                    root.setOnClickListener {
                        ivTick.isSelected = !ivTick.isSelected
                        listener(imageResponse)
                    }
                }
            }
        }
    }
}