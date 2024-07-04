package com.example.app2.database

import com.example.app2.database.model.ImageEntity
import kotlinx.coroutines.flow.Flow

class MainRepository(database: AppDatabase) {

    private val imageDao = database.imageDao()

    fun getAll(): Flow<List<ImageEntity>> {
        return imageDao.getAll()
    }

    private fun getAllImage(): List<ImageEntity> {
        return imageDao.getAllImage()
    }

    fun insert(imageEntity: ImageEntity) {
        imageDao.insert(imageEntity)
    }

    fun delete(id: String) {
        imageDao.delete(id)
    }

    fun isExisted(id: String): Boolean {
        return getAllImage().find {
            it.imageId == id
        } != null
    }
}