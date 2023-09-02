package fr.mjoudar.withingstest.domain.dto

import com.squareup.moshi.Json
import fr.mjoudar.withingstest.domain.models.ImageInfo

data class HitsItem(
    @Json(name = "webformatURL")
    val webformatURL: String = "",
    @Json(name = "previewURL")
    val previewURL: String = "",
    @Json(name = "tags")
    val tags: String = "",
    @Json(name = "downloads")
    val downloads: Int = 0,
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "views")
    val views: Int = 0,
    @Json(name = "likes")
    val likes: Int = 0
) {
    fun toImageInfo() = ImageInfo(
        id = id,
        previewURL = previewURL,
        url = webformatURL,
        views = views,
        likes = likes,
        downloads = downloads,
        tags = tags
    )
}