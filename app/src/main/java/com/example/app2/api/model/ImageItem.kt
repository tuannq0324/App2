package com.example.app2.api.model

import com.example.app2.model.QualityUrls


sealed class ImageResponse {
    data class Success(val items: List<ImageItem>) : ImageResponse()

    data object Failed: ImageResponse()
}

data class ImageItem(
    var id: String, var qualityUrls: QualityUrls
)