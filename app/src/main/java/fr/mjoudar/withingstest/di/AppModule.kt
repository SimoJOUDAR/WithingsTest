package fr.mjoudar.withingstest.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.mjoudar.withingstest.data.api.PixabayApi
import fr.mjoudar.withingstest.data.api.PixabayService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    private val BASE_URL = "https://pixabay.com/"

    @Singleton
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {
        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

        val client = OkHttpClient.Builder()
        client.followRedirects(false)
        client.followSslRedirects(false)
        client.connectTimeout(2, TimeUnit.SECONDS)
        client.callTimeout(5, TimeUnit.SECONDS)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun providePixabayService(retrofit: Retrofit): PixabayService = retrofit.create(PixabayService::class.java)

    @Singleton
    @Provides
    fun provideApiClient(service: PixabayService) = PixabayApi(service)
}