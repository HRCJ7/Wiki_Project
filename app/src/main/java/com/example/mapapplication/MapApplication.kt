package com.example.mapapplication

import android.app.Application
import com.example.mapapplication.di.component.ApplicationComponent
import com.example.mapapplication.di.component.DaggerApplicationComponent
import com.example.mapapplication.di.module.ApplicationModule

class MapApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }
}