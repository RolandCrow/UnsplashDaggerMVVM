package com.example.unsplashdaggermvvm.di.modules

import com.example.unsplashdaggermvvm.ui.ImagesListFragment
import com.example.unsplashdaggermvvm.ui.SearchFragment
import com.example.unsplashdaggermvvm.ui.SingleImageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeImageListFragment(): ImagesListFragment

    @ContributesAndroidInjector
    abstract fun contributeSingleImageFragment(): SingleImageFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}