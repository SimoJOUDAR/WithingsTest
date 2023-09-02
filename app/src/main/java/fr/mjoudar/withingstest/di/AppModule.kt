package fr.mjoudar.withingstest.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.mjoudar.withingstest.data.api.PixabayApi
import fr.mjoudar.withingstest.data.api.PixabayService
import fr.mjoudar.withingstest.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun providePixabayService(retrofit: Retrofit): PixabayService =
        retrofit.create(PixabayService::class.java)

    @Singleton
    @Provides
    fun provideApiClient(service: PixabayService) = PixabayApi(service)
}