package com.example.app2.view.second

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.app2.databinding.ItemRvImageBinding
import com.example.app2.model.ImageViewItem
import com.example.app2.utils.Constants.VIEW_TYPE_ITEM
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_MORE
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_MORE_FAILED

class SecondFragmentAdapter(
    private val data: ArrayList<ImageViewItem?>,
    private val listener: (ImageViewItem) -> Unit,
) : RecyclerView.Adapter<SecondViewHolder>() {

    private lateinit var binding: ItemRvImageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondViewHolder {

        binding = ItemRvImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SecondViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is ImageViewItem.LoadMore -> VIEW_TYPE_LOAD_MORE
            is ImageViewItem.LoadMoreFailed -> VIEW_TYPE_LOAD_MORE_FAILED
            else -> VIEW_TYPE_ITEM + position
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(newList: List<ImageViewItem>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SecondViewHolder, position: Int) {

        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan =
            getItemViewType(position) == VIEW_TYPE_LOAD_MORE || getItemViewType(position) == VIEW_TYPE_LOAD_MORE_FAILED
        data[position]?.let { holder.bind(it) }
    }
}