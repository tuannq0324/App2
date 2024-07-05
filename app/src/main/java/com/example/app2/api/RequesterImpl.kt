package com.example.app2.api

import android.util.Log
import com.example.app2.api.model.ImageItem
import com.example.app2.api.model.ImageResponse
import com.example.app2.model.QualityUrls
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RequesterImpl : IRequester {

    override suspend fun loadImages(page: Int, perPage: Int): Unit = withContext(Dispatchers.IO) {
        callApi(page = page, perPage = perPage)
    }

    override suspend fun loadMore(page: Int, perPage: Int): Unit = withContext(Dispatchers.IO) {
        callApi(page = page, perPage = perPage)
    }

    val response: MutableStateFlow<ImageResponse?> = MutableStateFlow<ImageResponse?>(null)

    private suspend fun callApi(page: Int, perPage: Int) {
        val url = "$BASE_URL$ACCESS_KEY&page=$page&per_page=${perPage}"

        response.emit(ImageResponse.Loading)

        try {
            val jsonString = getJsonStringFromURL(url)

            if (jsonString != null) {
                val jsonArray = JSONArray(jsonString)

                val items = jsonArray.parseData()

                response.emit(ImageResponse.Success(items))
            } else {
                response.emit(ImageResponse.Failed)
            }

        } catch (e: Throwable) {
            response.emit(ImageResponse.Failed)
        }
    }

    private fun JSONArray.parseData(): List<ImageItem> {
        val imageItems = mutableListOf<ImageItem>()

        for (i in 0 until this.length()) {
            val item = this.getJSONObject(i)

            val urls = item.getJSONObject("urls")

            val id = item.optString("id") ?: break

            val imageItem = ImageItem(
                id = id, qualityUrls = QualityUrls(
                    full = urls.optString("full") ?: "",
                    raw = urls.optString("raw") ?: "",
                    regular = urls.optString("regular") ?: "",
                    small = urls.optString("small") ?: "",
                    smallS3 = urls.optString("small_s3") ?: "",
                    thumb = urls.optString("thumb") ?: "",
                )
            )

            imageItems.add(imageItem)
        }

        return imageItems
    }

    @Throws(IOException::class, JSONException::class)
    private fun getJsonStringFromURL(urlString: String?): String? {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            Log.d("TAG", connection.responseCode.toString())

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                response.toString()

            } else {
                null
            }

        } catch (e: Throwable) {
            e.printStackTrace()
            null
        } finally {
            connection.disconnect()
        }
    }
}