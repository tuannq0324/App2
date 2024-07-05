package com.example.app2.activity.activity1

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app2.BaseApplication
import com.example.app2.api.model.ImageItem
import com.example.app2.api.model.ImageResponse
import com.example.app2.database.MainRepository
import com.example.app2.database.model.ImageEntity
import com.example.app2.model.ImageViewItem
import com.example.app2.utils.CommonFunction.convertToImageEntity
import com.example.app2.utils.CommonFunction.convertToImageResponse
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

    private val response: MutableStateFlow<List<ImageItem>> = MutableStateFlow(arrayListOf())

    val imageViewItems: Flow<List<ImageViewItem>> =
        response.combine(imageEntities) { imageItems, imageEntities ->

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
        Log.d("TAG", "MainViewModel: ")
    }

    fun fetchData() {
        viewModelScope.launch {

            isLoading = true

            val data = BaseApplication.requester.loadImages(page, 10)

            if (data is ImageResponse.Success) {
                val oldList = response.value

                val newList = oldList + data.items

                response.emit(newList.distinctBy { it.id })
            }

            isLoading = false
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