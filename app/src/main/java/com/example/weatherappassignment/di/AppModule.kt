package com.example.weatherappassignment.di

import com.example.weatherappassignment.data.WeatherDataSource
import com.example.weatherappassignment.data.WeatherRepository
import com.example.weatherappassignment.data.WeatherRepositoryImpl
import com.example.weatherappassignment.data.remote.WeatherApiService
import com.example.weatherappassignment.data.remote.WeatherNetworkDataSource
import com.example.weatherappassignment.utils.BASE_URL
import com.example.weatherappassignment.utils.ResourceProvider
import com.example.weatherappassignment.utils.ResourceProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@InstallIn(SingletonComponent::class)
@Module
interface AppModule {

    @Singleton
    @Binds
    fun bindResourceProvider(resourceProvider: ResourceProviderImpl): ResourceProvider

    @Singleton
    @Binds
    fun bindRepository(repository: WeatherRepositoryImpl): WeatherRepository

    @Singleton
    @Binds
    fun bindApiClient(apiClient: WeatherNetworkDataSource): WeatherDataSource

    companion object Provider {

        @Singleton
        @Provides
        fun provideUiCoroutineContext(): CoroutineContext = Dispatchers.Main

        @Singleton
        @Provides
        fun providesRetrofitInstance(): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        @Singleton
        @Provides
        fun providesApiService(retrofit: Retrofit): WeatherApiService = retrofit.create(WeatherApiService::class.java)
    }
}