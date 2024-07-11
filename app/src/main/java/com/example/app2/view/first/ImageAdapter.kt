package com.example.app2.view.first

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import coil.load
import coil.size.Precision
import coil.size.Scale
import com.example.app2.R
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
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

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

    class ImageViewHolder(
        private var binding: ViewBinding,
        private val listener: (ImageViewItem) -> Unit,
        private val tryAgain: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageResponse: ImageViewItem) {
            val spannableString =
                SpannableString(binding.root.context.getString(R.string.load_failed_try_again))
            spannableString.setSpan(
                ForegroundColorSpan(Color.BLUE),
                spannableString.indexOf("try"),
                spannableString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            when (binding) {

                is ItemLoadMoreFailedBinding -> {
                    (binding as ItemLoadMoreFailedBinding).apply {
                        tvTryAgain.text = spannableString
                        tvTryAgain.setOnClickListener {
                            tryAgain.invoke()
                        }
                    }
                }

                is ItemLoadMoreBinding -> {
                    (binding as ItemLoadMoreBinding).progressBar.isIndeterminate = true
                }

                is ItemRvImageBinding -> {
                    (binding as ItemRvImageBinding).apply {

                        val image = imageResponse as ImageViewItem.Image

                        ivTick.isSelected = image.item.isSelected == true

                        ivItem.load(imageResponse.item.item.urls.last()) {
                            placeholder(R.drawable.ic_image_default)
                            error(R.drawable.ic_load_failed)
                            crossfade(true)
                            memoryCacheKey(imageResponse.item.item.id)
                            precision(Precision.EXACT)
                            scale(Scale.FILL)
                        }

                        root.setOnClickListener {
                            ivTick.isSelected = !ivTick.isSelected
                            listener(imageResponse)
                        }
                    }
                }
            }
        }
    }
}