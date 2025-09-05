package com.example.unsplashdaggermvvm.di.modules

import dagger.Module

@Module(
    includes = [
        ViewModelModule::class,
        NetworkModule::class,
        DataModule::class
    ]
)
class AppModule