package fr.mjoudar.withingstest.data.api


import fr.mjoudar.withingstest.BuildConfig
import fr.mjoudar.withingstest.domain.dto.PixabayResponse
import fr.mjoudar.withingstest.utils.Constants.Companion.IMAGE_TYPE
import fr.mjoudar.withingstest.utils.Constants.Companion.PRETTY
import retrofit2.Response

class PixabayApi(private val service: PixabayService) {

    suspend fun getData(
        input: String
    ): SimpleResponse<PixabayResponse> {
        return safeApiCall {
            service.getData(
                BuildConfig.PIXABAY_API_KEY,
                input,
                IMAGE_TYPE,
                PRETTY
            )
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T> {
        return try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }
    }
}