package com.example.weatherappassignment.di

import com.example.weatherappassignment.data.Repository
import com.example.weatherappassignment.data.RepositoryImpl
import com.example.weatherappassignment.data.remote.ApiClient
import com.example.weatherappassignment.data.remote.ApiService
import com.example.weatherappassignment.data.remote.RetrofitApiClient
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
    fun bindRepository(repository: RepositoryImpl): Repository

    @Singleton
    @Binds
    fun bindApiClient(apiClient: RetrofitApiClient): ApiClient

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
        fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
    }
}