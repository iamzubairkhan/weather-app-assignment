package com.example.weatherappassignment.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface AppModule {
    // Bind your dependencies with Interfaces here
    companion object Provider {
        // Provide your dependencies here
    }
}