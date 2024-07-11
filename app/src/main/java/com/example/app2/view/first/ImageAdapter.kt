package com.example.app2.view.first

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.app2.databinding.ItemLoadMoreBinding
import com.example.app2.databinding.ItemLoadMoreFailedBinding
import com.example.app2.databinding.ItemRvImageBinding
import com.example.app2.model.ImageViewItem
import com.example.app2.utils.Constants.VIEW_TYPE_ITEM
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_MORE
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_MORE_FAILED

class ImageAdapter(
    private val data: ArrayList<ImageViewItem?>,
    private val listener: (ImageViewItem) -> Unit,
    private val tryAgain: () -> Unit,
) : RecyclerView.Adapter<ImageViewHolder>() {

    private lateinit var binding: ItemRvImageBinding
    private lateinit var bindingLoadMore: ItemLoadMoreBinding
    private lateinit var bindingLoadMoreFailed: ItemLoadMoreFailedBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        binding = ItemRvImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        bindingLoadMore =
            ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        bindingLoadMoreFailed =
            ItemLoadMoreFailedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return when (viewType) {
            VIEW_TYPE_LOAD_MORE -> ImageViewHolder(bindingLoadMore, listener, tryAgain)
            VIEW_TYPE_LOAD_MORE_FAILED -> ImageViewHolder(bindingLoadMoreFailed, listener, tryAgain)
            else -> ImageViewHolder(binding, listener, tryAgain)
        }
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

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = getItemViewType(position) == VIEW_TYPE_LOAD_MORE
                || getItemViewType(position) == VIEW_TYPE_LOAD_MORE_FAILED
        data[position]?.let { holder.bind(it) }
    }
}