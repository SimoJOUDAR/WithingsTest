package fr.mjoudar.withingstest.data.api

import fr.mjoudar.withingstest.domain.dto.ImageItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayService {

    @GET("keyword")
    suspend fun getData(@Query("keyword") keyword : String): Response<List<ImageItem>>
}