package fr.mjoudar.withingstest.domain.dto

import com.squareup.moshi.Json

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
)