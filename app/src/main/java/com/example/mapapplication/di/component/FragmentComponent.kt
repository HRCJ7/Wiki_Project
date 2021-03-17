package com.example.mapapplication.di.component

import com.example.mapapplication.di.FragmentScope
import com.example.mapapplication.di.module.FragmentModule
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent