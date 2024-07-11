package com.example.app2.view.first

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import coil.size.Precision
import coil.size.Scale
import com.example.app2.R
import com.example.app2.databinding.ItemLoadMoreBinding
import com.example.app2.databinding.ItemLoadMoreFailedBinding
import com.example.app2.databinding.ItemRvImageBinding
import com.example.app2.model.ImageViewItem

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