package com.example.app2.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_image")
data class ImageEntity(
    @PrimaryKey
    @ColumnInfo("imageId")
    var imageId: String,
    @ColumnInfo("urlFull")
    val urlFull: String?,
    @ColumnInfo("urlRaw")
    val urlRaw: String?,
    @ColumnInfo("urlRegular")
    val urlRegular: String?,
    @ColumnInfo("urlSmall")
    val urlSmall: String?,
    @ColumnInfo("urlSmallS3")
    val urlSmallS3: String?,
    @ColumnInfo("urlThumb")
    val urlThumb: String?,
)