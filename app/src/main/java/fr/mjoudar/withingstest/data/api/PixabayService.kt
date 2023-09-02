package fr.mjoudar.withingstest.data.api

import fr.mjoudar.withingstest.domain.dto.PixabayResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayService {

    @GET("api/")
    suspend fun getData(
        @Query("key") key: String,
        @Query("q") input: String,
        @Query("image_type") imageType: String,
        @Query("pretty") pretty: String
    ): Response<PixabayResponse>

}