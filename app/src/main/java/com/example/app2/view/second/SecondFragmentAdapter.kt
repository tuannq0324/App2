package com.example.app2.view.second

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app2.databinding.ItemRvImageBinding
import com.example.app2.model.ImageViewItem
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_FAILED
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_MORE
import com.example.app2.utils.Constants.VIEW_TYPE_LOAD_MORE_FAILED

class SecondFragmentAdapter(
    private val data: ArrayList<ImageViewItem?>,
    private val listener: (ImageViewItem) -> Unit,
) : RecyclerView.Adapter<SecondFragmentAdapter.ImageViewHolder>() {

    private lateinit var binding: ItemRvImageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        binding = ItemRvImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE || getItemViewType(
                            position
                        ) == VIEW_TYPE_LOAD_FAILED || getItemViewType(position) == VIEW_TYPE_LOAD_MORE_FAILED
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
        private var binding: ItemRvImageBinding,
        private val listener: (ImageViewItem) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageResponse: ImageViewItem) {
            binding.apply {

                val item = (imageResponse as ImageViewItem.Image).item

                ivTick.isSelected = item.isSelected == true

                Glide.with(root.context).load(imageResponse.item.item.qualityUrls?.thumb).into(ivItem)

                root.setOnClickListener {
                    ivTick.isSelected = !ivTick.isSelected
                    listener(imageResponse)
                }
            }

        }
    }
}