package com.example.app2.view.second

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Precision
import coil.size.Scale
import com.example.app2.R
import com.example.app2.databinding.ItemRvImageBinding
import com.example.app2.model.ImageViewItem

class SecondViewHolder(
    private var binding: ItemRvImageBinding,
    private val listener: (ImageViewItem) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(imageResponse: ImageViewItem) {
        binding.apply {

            val item = (imageResponse as ImageViewItem.Image).item

            ivTick.isSelected = item.isSelected == true

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