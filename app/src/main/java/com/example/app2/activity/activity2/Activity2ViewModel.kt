package com.example.app2.activity.activity2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app2.database.MainRepository
import com.example.app2.model.ImageViewItem
import com.example.app2.utils.CommonFunction.convertToImageEntity
import com.example.app2.utils.CommonFunction.convertToImageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

//@HiltViewModel
class Activity2ViewModel(private val repository: MainRepository) : ViewModel() {

    val imageViewItems = repository.getAll().map { list ->
        list.map {
            ImageViewItem(item = it.convertToImageResponse(), isSelected = true)
        }
    }

    fun updateSelect(imageItem: ImageViewItem) {
        viewModelScope.launch(Dispatchers.IO) {
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