package fr.mjoudar.withingstest.domain.dto

import com.squareup.moshi.Json

data class PixabayResponse(
    @Json(name = "hits")
    val hits: List<HitsItem>?
)