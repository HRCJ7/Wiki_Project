package com.example.mapapplication.di.component

import com.example.mapapplication.di.ActivityScope
import com.example.mapapplication.di.module.ActivityModule
import com.example.mapapplication.ui.details.DetailActivity
import com.example.mapapplication.ui.direction.DirectionActivity
import com.example.mapapplication.ui.map.MapActivity
import dagger.Component

@ActivityScope
@Component(
        dependencies = [ApplicationComponent::class],
        modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun inject(activity: MapActivity)

    fun inject(activity: DetailActivity)

    fun inject(activity: DirectionActivity)
}