package fr.mjoudar.withingstest.data.api


import fr.mjoudar.withingstest.domain.dto.ImageItem
import retrofit2.Response
import javax.inject.Inject

class PixabayApi(private val service: PixabayService) {

    suspend fun getData(keyword : String) : SimpleResponse<List<ImageItem>> {
        return safeApiCall { service.getData(keyword) }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T> {
        return try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }
    }
}