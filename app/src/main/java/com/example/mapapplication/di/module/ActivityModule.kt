package com.example.mapapplication.di.module

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapapplication.data.repository.MapRepository
import com.example.mapapplication.ui.base.BaseActivity
import com.example.mapapplication.ui.details.DetailViewModel
import com.example.mapapplication.ui.details.ImageAdapter
import com.example.mapapplication.ui.direction.DirectionViewModel
import com.example.mapapplication.ui.map.MapViewModel
import com.example.mapapplication.utils.ViewModelProviderFactory
import com.example.mapapplication.utils.network.NetworkHelper
import com.example.mapapplication.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Kotlin Generics Reference: https://kotlinlang.org/docs/reference/generics.html
 * Basically it means that we can pass any class that extends BaseActivity which take
 * BaseViewModel subclass as parameter
 */
@Module
class ActivityModule(private val activity: BaseActivity<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

    @Provides
    fun provideMapViewModel(
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable,
            networkHelper: NetworkHelper,
            mapRepository: MapRepository
    ): MapViewModel = ViewModelProviders.of(
            activity, ViewModelProviderFactory(MapViewModel::class) {
        MapViewModel(schedulerProvider, compositeDisposable, networkHelper, mapRepository)
    }).get(MapViewModel::class.java)

    @Provides
    fun provideDetailViewModel(
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable,
            networkHelper: NetworkHelper,
            mapRepository: MapRepository
    ): DetailViewModel = ViewModelProviders.of(
            activity, ViewModelProviderFactory(DetailViewModel::class) {
        DetailViewModel(schedulerProvider, compositeDisposable, networkHelper, mapRepository)
    }).get(DetailViewModel::class.java)

    @Provides
    fun provideDirectionViewModel(
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable,
            networkHelper: NetworkHelper,
            mapRepository: MapRepository
    ): DirectionViewModel = ViewModelProviders.of(
            activity, ViewModelProviderFactory(DirectionViewModel::class) {
        DirectionViewModel(schedulerProvider, compositeDisposable, networkHelper, mapRepository)
    }).get(DirectionViewModel::class.java)

    @Provides
    fun provideImageAdapter() =
            ImageAdapter(
                    activity.lifecycle,
                    ArrayList()
            )
}