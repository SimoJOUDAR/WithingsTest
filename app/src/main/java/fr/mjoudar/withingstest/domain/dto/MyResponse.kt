package fr.mjoudar.withingstest.domain.dto

import com.squareup.moshi.Json
import fr.mjoudar.withingstest.domain.models.ImageInfo

data class ImageItem(
    @Json(name = "pageURL")
    val url: String,
    @Json(name = "pageURL")
    val views: Long,
    @Json(name = "pageURL")
    val likes: Long,
    @Json(name = "pageURL")
    val downloads: Long,
    @Json(name = "pageURL")
    val tags: String,
) {

    fun toImageInfo() = ImageInfo(url = url, views = views, likes = likes, downloads = downloads, tags = tags )
}