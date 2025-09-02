package com.example.unsplashdaggermvvm.di.modules

import com.example.unsplashdaggermvvm.api.UnsplashService
import com.example.unsplashdaggermvvm.repository.UnsplashRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {

    @Singleton
    @Provides
    fun providesRepository(unsplashService: UnsplashService): UnsplashRepository =
        UnsplashRepository(unsplashService = unsplashService)
}