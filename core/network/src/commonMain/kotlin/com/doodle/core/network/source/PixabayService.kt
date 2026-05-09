package com.doodle.core.network.source

import com.doodle.core.domain.model.remote.RemoteImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PixabayService(
    private val client: HttpClient,
    private val apiKey: String,
    private val defaultLang: String = "en"
) {
    // TODO: Refactor to 7 parameters
    suspend fun getImagesByPage(
        pageKey: Int = 1,
        q: String? = null,
        imageType: String = "photo",
        lang: String = defaultLang,
        orientation: String = "vertical",
        category: String? = null,
        order: String? = null,
        perPage: Int = 20,
        safesearch: Boolean = true,
        editorsChoice: Boolean = false
    ): RemoteImage {
        return client.get("api") {
            parameter("key", apiKey)
            parameter("page", pageKey)
            parameter("q", q)
            parameter("image_type", imageType)
            parameter("lang", lang)
            parameter("orientation", orientation)
            parameter("category", category)
            parameter("order", order)
            parameter("per_page", perPage)
            parameter("safesearch", safesearch)
            parameter("editors_choice", editorsChoice)
        }.body()
    }
}
