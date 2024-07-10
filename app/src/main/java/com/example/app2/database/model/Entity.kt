package com.example.app2.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_image")
data class ImageEntity(
    @PrimaryKey
    @ColumnInfo("imageId")
    var imageId: String,
    @ColumnInfo("urls")
    val qualityUrls: List<String>
)