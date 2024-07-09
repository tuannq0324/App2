package com.example.app2.view.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app2.database.MainRepository
import com.example.app2.view.BaseViewModel

class SecondViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecondViewModel::class.java)) {
            return SecondViewModel(repository = repository) as T
        }

        throw IllegalArgumentException("Cannot create an instance of $modelClass")
    }
}


class SecondViewModel(repository: MainRepository): BaseViewModel(repository)