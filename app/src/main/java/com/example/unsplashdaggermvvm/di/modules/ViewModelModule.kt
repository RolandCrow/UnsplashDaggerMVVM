package com.example.unsplashdaggermvvm.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unsplashdaggermvvm.viewmodels.ImagesListViewModel
import com.example.unsplashdaggermvvm.viewmodels.SearchViewModel
import com.example.unsplashdaggermvvm.viewmodels.SingleImageViewModel
import com.example.unsplashdaggermvvm.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ImagesListViewModel::class)
    abstract fun bindImagesListViewModel(imagesListViewModel: ImagesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SingleImageViewModel::class)
    abstract fun bindSingleImageViewModel(singleImageViewModel: SingleImageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)