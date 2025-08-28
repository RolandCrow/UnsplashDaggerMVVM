package com.example.unsplashdaggermvvm.di

import android.app.Application
import com.example.unsplashdaggermvvm.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,

    ]
)

interface AppComponent: AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    override fun inject(instance: App?)
}