package com.example.app2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app2.database.model.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM tbl_image")
    fun getAll(): Flow<List<ImageEntity>>

    @Query("SELECT * FROM tbl_image")
    fun getAllImage(): List<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imageEntity: ImageEntity)

    @Query("DELETE FROM tbl_image WHERE imageId = :id")
    fun delete(id: String)

}