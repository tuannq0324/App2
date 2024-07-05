package com.example.app2.activity.activity1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app2.BaseApplication.Companion.requester
import com.example.app2.api.model.ImageItem
import com.example.app2.api.model.ImageResponse
import com.example.app2.database.MainRepository
import com.example.app2.database.model.ImageEntity
import com.example.app2.model.ImageViewItem
import com.example.app2.utils.CommonFunction.convertToImageEntity
import com.example.app2.utils.CommonFunction.convertToImageResponse
import com.example.app2.utils.Constants.LOAD_FAILED
import com.example.app2.utils.Constants.LOAD_MORE
import com.example.app2.utils.Constants.LOAD_MORE_FAILED
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

//@HiltViewModel
open class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    private val imageEntities: Flow<List<ImageEntity>> = repository.getAll()

    private val imageItems: MutableStateFlow<List<ImageItem>> = MutableStateFlow(arrayListOf())

    val imageViewItems: Flow<List<ImageViewItem>> =
        imageItems.combine(imageEntities) { imageItems, imageEntities ->

            val selected = imageEntities.map { it.imageId }

            imageItems.map {
                ImageViewItem(item = it, isSelected = selected.contains(it.id))
            }
        }

    open val imageViewItemsSelected = repository.getAll().map { list ->
        list.map {
            ImageViewItem(item = it.convertToImageResponse(), isSelected = true)
        }
    }

    private var page = 1

    private var isLoading = false

    init {
        viewModelScope.launch {
            requester.response.collect { imageResponse ->
                imageResponse ?: return@collect

                val currentList = imageItems.value.toMutableList()

                when (imageResponse) {

                    is ImageResponse.Success -> {
                        delay(1000L)

                        currentList.removeIf { it.id == LOAD_MORE || it.id == LOAD_FAILED || it.id == LOAD_MORE_FAILED }
                        currentList.addAll(imageResponse.items)

                        imageItems.emit(currentList.distinctBy { it.id })

                        onFinish.invoke(true)
                        isLoading = false
                    }

                    is ImageResponse.Failed -> {
                        if (currentList.isEmpty()) {
                            //load failed
                            currentList += ImageItem(id = LOAD_FAILED, qualityUrls = null)
                            imageItems.emit(currentList)
                        } else {
                            //load more failed
                            currentList.removeIf { it.id == LOAD_MORE }

                            if (currentList.last().id != LOAD_FAILED && currentList.last().id != LOAD_MORE_FAILED) {
                                currentList += ImageItem(id = LOAD_MORE_FAILED, qualityUrls = null)
                                imageItems.emit(currentList)
                            }
                        }

                        onFinish.invoke(false)
                        isLoading = false
                    }

                    is ImageResponse.Loading -> {
                        isLoading = true
                        if (currentList.isNotEmpty()) {
                            if (currentList.last().id != LOAD_MORE && currentList.last().id != LOAD_FAILED && currentList.last().id != LOAD_MORE_FAILED) {
                                currentList += ImageItem(id = LOAD_MORE, qualityUrls = null)
                                imageItems.emit(currentList)
                            }
                        }
                    }
                }
            }
        }
    }

    private var onFinish: (isSuccess: Boolean) -> Unit = {}

    fun fetchData(onFinish: (isChanged: Boolean) -> Unit = {}) {
        this.onFinish = onFinish
        viewModelScope.launch {
            if (isLoading) return@launch

            requester.loadImages(page, 10)
        }
    }

    fun loadMore() {
        if (isLoading) return

        page++
        fetchData()
    }

    fun updateSelect(imageItem: ImageViewItem) {
        viewModelScope.launch {
            val item = imageItem.item

            val isExist = repository.isExisted(id = item.id)

            if (isExist) {
                repository.delete(item.id)
            } else {
                repository.insert(item.convertToImageEntity())
            }
        }
    }
}