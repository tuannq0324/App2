package com.example.app2.view.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app2.activity.activity1.MainViewModel
import com.example.app2.database.MainRepository

class FirstViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirstViewModel::class.java)) {
            return FirstViewModel(repository = repository) as T
        }

        throw IllegalArgumentException("Cannot create an instance of $modelClass")
    }
}


class FirstViewModel(repository: MainRepository): MainViewModel(repository)