package com.example.app2.utils

import com.example.app2.api.model.ImageItem
import com.example.app2.database.model.ImageEntity
import com.example.app2.model.QualityUrls

object CommonFunction {

    fun ImageEntity.convertToImageResponse(): ImageItem = ImageItem(
        imageId, QualityUrls(urlFull, urlRaw, urlRegular, urlSmall, urlSmallS3, urlThumb)
    )

//    fun List<ImageEntity>.convertToListImageResponse(): List<ImageItem> {
//        val list = arrayListOf<ImageItem>()
//        mapTo(list){
//            it.convertToImageResponse()
//        }
//        return list
//    }

    fun ImageItem.convertToImageEntity(): ImageEntity = ImageEntity(
        imageId = id,
        urlFull = qualityUrls.full,
        urlRaw = qualityUrls.raw,
        urlRegular = qualityUrls.regular,
        urlSmall =  qualityUrls.small,
        urlSmallS3 = qualityUrls.smallS3,
        urlThumb =  qualityUrls.thumb,
    )

//    fun List<ImageItem>.convertToListImageEntity(): List<ImageEntity> {
//        val list = arrayListOf<ImageEntity>()
//        mapTo(list){
//            it.convertToImageEntity()
//        }
//        return list
//    }

}