package fr.mjoudar.withingstest.data.api


import fr.mjoudar.withingstest.BuildConfig
import fr.mjoudar.withingstest.data.api.SimpleResponse.Companion.safeApiCall
import fr.mjoudar.withingstest.utils.Constants.Companion.IMAGE_TYPE
import fr.mjoudar.withingstest.utils.Constants.Companion.PRETTY

class PixabayApi(private val service: PixabayService) {

    suspend fun getData(input: String) =
        safeApiCall {
            service.getData(
                BuildConfig.PIXABAY_API_KEY,
                input,
                IMAGE_TYPE,
                PRETTY
            )
        }
}