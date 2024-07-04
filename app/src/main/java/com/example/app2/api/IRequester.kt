package com.example.app2.api

import com.example.app2.api.model.ImageResponse

interface IRequester {
    suspend fun loadImages(page: Int, perPage: Int) : ImageResponse

    suspend fun loadMore(page: Int, perPage: Int = 10) : ImageResponse
}