package fr.mjoudar.withingstest.utils

import fr.mjoudar.withingstest.domain.dto.HitsItem
import fr.mjoudar.withingstest.domain.dto.PixabayResponse
import fr.mjoudar.withingstest.domain.models.ImageInfo

fun HitsItem.toImageInfo() =
    ImageInfo(
        id = id,
        previewURL = previewURL,
        url = webformatURL,
        views = views,
        likes = likes,
        downloads = downloads,
        tags = tags
    )

fun PixabayResponse.toImageInfo() =
    hits?.map { it.toImageInfo() }?.toMutableList() ?: mutableListOf()