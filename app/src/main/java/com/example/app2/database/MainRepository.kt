package com.example.app2.database

import com.example.app2.database.model.ImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MainRepository(database: AppDatabase) {

    private val imageDao = database.imageDao()

    fun getAll(): Flow<List<ImageEntity>> {
        return imageDao.getAll()
    }

    suspend fun insert(imageEntity: ImageEntity)  = withContext(Dispatchers.IO){
        imageDao.insert(imageEntity)
    }

    suspend fun delete(id: String) = withContext(Dispatchers.IO){
        imageDao.delete(id)
    }

    suspend fun isExisted(id: String): Boolean = withContext(Dispatchers.IO) {
        getAllImage().find {
            it.imageId == id
        } != null
    }

    private fun getAllImage(): List<ImageEntity> {
        return imageDao.getAllImage()
    }
}