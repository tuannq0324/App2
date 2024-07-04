package com.example.app2.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app2.database.MainRepository

class ViewModelFactory(private val repository: MainRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            modelClass.getConstructor(MainRepository::class.java).newInstance(repository)
        } catch (e: Throwable){
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }

    }
}