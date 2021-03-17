package com.example.mapapplication.ui.details

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.mapapplication.R
import com.example.mapapplication.di.component.ViewHolderComponent
import com.example.mapapplication.ui.base.BaseItemViewHolder
import kotlinx.android.synthetic.main.item_view_image.view.*

class ImageItemViewHolder(parent: ViewGroup) :
        BaseItemViewHolder<String, ImageItemViewModel>(R.layout.item_view_image, parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.url.observe(this, Observer {
            Glide.with(itemView.context).load(it).into(itemView.article_image)
        })
    }

    override fun setupView(view: View) {
        view.setOnClickListener {
            viewModel.onItemClick(adapterPosition)
        }
    }
}