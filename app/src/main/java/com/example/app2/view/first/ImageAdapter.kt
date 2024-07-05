package com.example.app2.view.first

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.app2.R
import com.example.app2.databinding.ItemLoadFailedBinding
import com.example.app2.databinding.ItemLoadMoreBinding
import com.example.app2.databinding.ItemLoadMoreFailedBinding
import com.example.app2.databinding.ItemRvImageBinding
import com.example.app2.model.ImageViewItem
import com.example.app2.utils.Constants.LOAD_FAILED
import com.example.app2.utils.Constants.LOAD_MORE
import com.example.app2.utils.Constants.LOAD_MORE_FAILED
import com.example.app2.utils.Constants.VIEW_TYPE_ITEM
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_FAILED
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_MORE
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_MORE_FAILED

class ImageAdapter(
    private val data: ArrayList<ImageViewItem?>,
    private val listener: (ImageViewItem) -> Unit,
    private val tryAgain: () -> Unit,
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private lateinit var binding: ItemRvImageBinding
    private lateinit var bindingLoadMore: ItemLoadMoreBinding
    private lateinit var bindingLoadFailed: ItemLoadFailedBinding
    private lateinit var bindingLoadMoreFailed: ItemLoadMoreFailedBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        binding = ItemRvImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        bindingLoadFailed =
            ItemLoadFailedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        bindingLoadMore =
            ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        bindingLoadMoreFailed =
            ItemLoadMoreFailedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.d("TAG", "onCreateViewHolder: $viewType")
        return when (viewType) {
            VIEW_TYPE_ITEM -> ImageViewHolder(binding, listener, tryAgain)
            VIEW_TYPE_LOAD_MORE -> ImageViewHolder(bindingLoadMore, listener, tryAgain)
            VIEW_TYPE_LOAD_FAILED -> ImageViewHolder(bindingLoadFailed, listener, tryAgain)
            VIEW_TYPE_LOAD_MORE_FAILED -> ImageViewHolder(bindingLoadMoreFailed, listener, tryAgain)
            else -> ImageViewHolder(binding, listener, tryAgain)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]?.item?.id) {
            LOAD_MORE -> VIEW_TYPE_LOAD_MORE
            LOAD_FAILED -> VIEW_TYPE_LOAD_FAILED
            LOAD_MORE_FAILED -> VIEW_TYPE_LOAD_MORE_FAILED
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE
                        || getItemViewType(position) == VIEW_TYPE_LOAD_FAILED
                        || getItemViewType(position) == VIEW_TYPE_LOAD_MORE_FAILED
                    ) 2 else 1
                }
            }
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
                is ItemLoadFailedBinding -> {
                    (binding as ItemLoadFailedBinding).apply {
                        tvTryAgain.text = spannableString
                        tvTryAgain.setOnClickListener {
                            tryAgain.invoke()
                        }
                    }
                }

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

                        ivTick.isSelected = imageResponse.isSelected == true

                        Glide.with(root.context).load(imageResponse.item.qualityUrls?.regular)
                            .into(ivItem)

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