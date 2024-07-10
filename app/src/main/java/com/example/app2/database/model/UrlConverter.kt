package com.example.app2.database.model

import androidx.room.TypeConverter
import org.json.JSONArray


class UrlConverter {
    @TypeConverter
    fun fromQualityUrls(items: List<String>): String {
        val jsonArray = JSONArray()
        for (item in items) {
            jsonArray.put(item)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toQualityUrls(value: String): List<String> {
        val items = mutableListOf<String>()
        val jsonArray = JSONArray(value)

        for (i in 0 until jsonArray.length()) {
            val url = jsonArray.optString(i)
            items.add(url)
        }

        return items
    }
}