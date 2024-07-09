package com.example.app2.model

import com.example.app2.api.model.ImageItem

sealed class ImageViewItem {
    data class Image(val item: DataItem) : ImageViewItem()

    data object LoadMore : ImageViewItem()

    data object LoadMoreFailed : ImageViewItem()
}

val ImageViewItem.id: String
    get() {
        return when(this) {
            is ImageViewItem.Image -> item.item.id
            ImageViewItem.LoadMore -> "LOAD_MORE"
            ImageViewItem.LoadMoreFailed -> "LOAD_MORE_FAILED"
        }
    }

data class DataItem(val item: ImageItem, var isSelected: Boolean = false)