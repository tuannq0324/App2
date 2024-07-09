package com.example.app2.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app2.BaseApplication.Companion.requester
import com.example.app2.api.model.ImageItem
import com.example.app2.api.model.ImageResponse
import com.example.app2.database.MainRepository
import com.example.app2.database.model.ImageEntity
import com.example.app2.model.DataItem
import com.example.app2.model.ImageViewItem
import com.example.app2.utils.CommonFunction.convertToImageEntity
import com.example.app2.utils.CommonFunction.convertToImageResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

enum class ViewState {
    Loading, Success, Failed, Empty,
}

enum class LoadMoreState {
    Idle, Loading, Success, Failed,
}

open class BaseViewModel(
    private val repository: MainRepository
) : ViewModel() {
    private val imageEntities: Flow<List<ImageEntity>> = repository.getAll()

    private val imageItems: MutableStateFlow<List<ImageItem>> = MutableStateFlow(arrayListOf())

    private val _viewState = MutableStateFlow(ViewState.Loading)
    val viewState: Flow<ViewState> = _viewState

    private val loadMore = MutableStateFlow(LoadMoreState.Idle)

    val imageViewItems: Flow<List<ImageViewItem>> =
        combine(imageItems, imageEntities, loadMore) { imageItems, imageEntities, loadMoreState ->
            val result = mutableListOf<ImageViewItem>()

            val selected = imageEntities.map { it.imageId }

            val map = imageItems.map {
                ImageViewItem.Image(
                    item = DataItem(
                        item = it,
                        isSelected = selected.contains(it.id)
                    )
                )
            }

            result.addAll(map)

            when (loadMoreState) {
                LoadMoreState.Loading -> result.add(ImageViewItem.LoadMore)
                LoadMoreState.Failed -> result.add(ImageViewItem.LoadMoreFailed)
                else -> {}
            }

            result
        }

    val imageViewItemsSelected: Flow<List<ImageViewItem.Image>> = repository.getAll().map { list ->
        list.map {
            ImageViewItem.Image(DataItem(item = it.convertToImageResponse(), isSelected = true))
        }
    }

    private var page = 1

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            updateViewState(ViewState.Loading)

            Log.d("TAG", "fetchData: Data")

            val currentList = imageItems.value.toMutableList()

            when (val imageResponse = requester.loadImages(page, 10)) {

                is ImageResponse.Success -> {
                    delay(3000)

                    page++

                    updateViewState(ViewState.Success)

                    currentList.addAll(imageResponse.items)

                    updateImageItems(items = currentList)
                }

                is ImageResponse.Failed -> {
                    delay(3000)

                    updateViewState(ViewState.Failed)
                }
            }
        }
    }

    fun loadMore() {
        if (loadMore.value == LoadMoreState.Loading) return

        updateLoadMoreState(LoadMoreState.Loading)

        viewModelScope.launch {

            delay(3000)

            val currentList = imageItems.value.toMutableList()

            when (val imageResponse = requester.loadImages(page, 10)) {
                is ImageResponse.Success -> {

                    page++

                    updateLoadMoreState(LoadMoreState.Success)

                    currentList.addAll(imageResponse.items)

                    updateImageItems(items = currentList)
                }

                ImageResponse.Failed -> {
                    updateLoadMoreState(LoadMoreState.Failed)
                }
            }
        }
    }

    fun updateSelect(imageItem: ImageViewItem) {
        viewModelScope.launch {
            if (imageItem is ImageViewItem.Image) {

                val item = imageItem.item

                val id = item.item.id

                val isExist = repository.isExisted(id = id)

                if (isExist) {
                    repository.delete(id)
                } else {
                    repository.insert(item.item.convertToImageEntity())
                }
            }
        }
    }

    private fun updateImageItems(items: List<ImageItem>) {
        imageItems.value = items.distinctBy {
            it.id
        }
        Log.d("TAG", "updateImageItems: ${items.size}")
    }

    private fun updateViewState(state: ViewState) {
        _viewState.value = state
    }

    private fun updateLoadMoreState(state: LoadMoreState) {
        loadMore.value = state
    }
}