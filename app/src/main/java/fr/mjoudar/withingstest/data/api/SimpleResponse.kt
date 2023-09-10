package fr.mjoudar.withingstest.data.api

import fr.mjoudar.withingstest.utils.Constants.Companion.UNKNOWN_EXCEPTION
import retrofit2.Response

data class SimpleResponse<T>(
    val status: Status,
    val data: Response<T>?,
    val exception: Exception?
) {

    sealed class Status {
        object Success : Status()
        object Failure : Status()
    }

    val failed: Boolean
        get() = status == Status.Failure

    val succeeded: Boolean
        get() = !failed && data?.isSuccessful == true

    val body: T
        get() = data!!.body()!!

    companion object {
        fun <T> success(data: Response<T>) = SimpleResponse(
            status = Status.Success,
            data = data,
            exception = null
        )

        fun <T> failure(exception: Exception?) = SimpleResponse<T>(
            status = Status.Failure,
            data = null,
            exception = exception ?: UNKNOWN_EXCEPTION
        )

        inline fun <T> safeApiCall(apiCall: () -> Response<T>) =
            try {
                success(apiCall.invoke())
            } catch (e: Exception) {
                failure(e)
            }
    }
}