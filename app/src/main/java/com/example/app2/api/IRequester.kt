package com.example.app2.api

interface IRequester {
    suspend fun loadImages(page: Int, perPage: Int)

    suspend fun loadMore(page: Int, perPage: Int = 10)
}