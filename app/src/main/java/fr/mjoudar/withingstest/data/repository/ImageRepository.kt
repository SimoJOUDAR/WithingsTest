package fr.mjoudar.withingstest.data.repository

import fr.mjoudar.withingstest.data.api.PixabayApi
import javax.inject.Inject

class ImageRepository @Inject constructor (private val pixabayApi: PixabayApi) {

    suspend fun getData(keyword: String) = pixabayApi.getData(keyword)
}