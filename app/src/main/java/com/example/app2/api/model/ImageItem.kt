package com.example.app2.api.model


sealed class ImageResponse {
    data class Success(val items: List<ImageItem>) : ImageResponse()

    data object Failed : ImageResponse()
}

data class ImageItem(
    val id: String, val urls: List<String>
)