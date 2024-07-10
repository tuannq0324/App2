package com.example.app2.api

import android.util.Log
import com.example.app2.api.model.ImageItem
import com.example.app2.api.model.ImageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RequesterImpl : IRequester {

    override suspend fun loadImages(page: Int, perPage: Int): ImageResponse =
        withContext(Dispatchers.IO) {
            callApi(page = page, perPage = perPage)
        }

    override suspend fun loadMore(page: Int, perPage: Int): ImageResponse =
        withContext(Dispatchers.IO) {
            callApi(page = page, perPage = perPage)
        }


    private fun callApi(page: Int, perPage: Int): ImageResponse {
        return try {
            val url = "$BASE_URL$ACCESS_KEY&page=$page&per_page=${perPage}"
            val results = getJsonStringFromURL(url)

            when (results.keys.first()) {
                HttpURLConnection.HTTP_OK -> {
                    val jsonString = results.values.first()
                    if (jsonString != null) {
                        val jsonArray = JSONArray(jsonString)

                        val items = jsonArray.parseData()

                        ImageResponse.Success(items)
                    } else {
                        ImageResponse.Failed
                    }
                }

                else -> {
                    ImageResponse.Failed
                }
            }
        } catch (e: Throwable) {
            ImageResponse.Failed
        }
    }
}

private fun JSONArray.parseData(): List<ImageItem> {
    val imageItems = mutableListOf<ImageItem>()

    for (i in 0 until this.length()) {
        val item = this.getJSONObject(i)

        val urls = item.getJSONObject("urls")

        val id = item.optString("id") ?: break

        val imageItem = ImageItem(
            id = id,
            urls = listOf(
                urls.optString("full") ?: "",
                urls.optString("raw") ?: "",
                urls.optString("regular") ?: "",
                urls.optString("small") ?: "",
                urls.optString("small_s3") ?: "",
                urls.optString("thumb") ?: "",
            )
        )

        imageItems.add(imageItem)
    }

    return imageItems
}

@Throws(IOException::class, JSONException::class)
private fun getJsonStringFromURL(urlString: String?): Map<Int, String?> {
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    val map = mutableMapOf<Int, String?>()

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
            map[responseCode] = response.toString()
            map

        } else {
            map[responseCode] = null
            map
        }

    } catch (e: Throwable) {
        e.printStackTrace()
        map[connection.responseCode] = null
        map
    } finally {
        connection.disconnect()
    }
}