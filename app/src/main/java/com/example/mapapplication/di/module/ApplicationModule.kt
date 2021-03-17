package com.example.mapapplication.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.mapapplication.BuildConfig
import com.example.mapapplication.MapApplication
import com.example.mapapplication.data.remote.NetworkService
import com.example.mapapplication.data.remote.Networking
import com.example.mapapplication.di.ApplicationContext
import com.example.mapapplication.utils.network.NetworkHelper
import com.example.mapapplication.utils.network.NetworkHelperImpl
import com.example.mapapplication.utils.rx.RxSchedulerProvider
import com.example.mapapplication.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: MapApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application

    /**
     * Since this function do not have @Singleton then each time CompositeDisposable is injected
     * then a new instance of CompositeDisposable will be provided
     */
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = RxSchedulerProvider()

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
            application.getSharedPreferences("wiki--project-prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService =
            Networking.create(
                    BuildConfig.BASE_URL,
                    application.cacheDir,
                    10 * 1024 * 1024 // 10MB
            )

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper = NetworkHelperImpl(application)
}