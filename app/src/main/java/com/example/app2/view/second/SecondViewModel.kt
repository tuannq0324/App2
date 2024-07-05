package com.example.app2.view.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app2.activity.activity1.MainViewModel
import com.example.app2.database.MainRepository

class SecondViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecondViewModel::class.java)) {
            return SecondViewModel(repository = repository) as T
        }

        throw IllegalArgumentException("Cannot create an instance of $modelClass")
    }
}


class SecondViewModel(private val repository: MainRepository): MainViewModel(repository)