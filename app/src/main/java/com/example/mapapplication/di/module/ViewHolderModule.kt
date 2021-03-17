package com.example.mapapplication.di.module

import androidx.lifecycle.LifecycleRegistry
import com.example.mapapplication.di.ViewModelScope
import com.example.mapapplication.ui.base.BaseItemViewHolder
import dagger.Module
import dagger.Provides

@Module
class ViewHolderModule(private val viewHolder: BaseItemViewHolder<*, *>) {

    @Provides
    @ViewModelScope
    fun provideLifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(viewHolder)
}