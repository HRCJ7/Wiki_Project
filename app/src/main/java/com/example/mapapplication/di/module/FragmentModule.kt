package com.example.mapapplication.di.module

import com.example.mapapplication.ui.base.BaseFragment
import dagger.Module

@Module
class FragmentModule(private val fragment: BaseFragment<*>)