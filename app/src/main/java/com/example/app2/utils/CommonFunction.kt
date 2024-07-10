package com.example.app2.utils

import com.example.app2.api.model.ImageItem
import com.example.app2.database.model.ImageEntity

object CommonFunction {

    fun ImageEntity.convertToImageResponse(): ImageItem = ImageItem(
        id = imageId, urls = qualityUrls
    )

    fun ImageItem.convertToImageEntity(): ImageEntity = ImageEntity(
        imageId = id,
        qualityUrls = urls
    )

}