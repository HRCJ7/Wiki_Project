package com.example.mapapplication.di.component

import com.example.mapapplication.di.ViewModelScope
import com.example.mapapplication.di.module.ViewHolderModule
import com.example.mapapplication.ui.details.ImageItemViewHolder
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {

    fun inject(viewHolder: ImageItemViewHolder)
}