package com.example.mapapplication.ui.details

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.example.mapapplication.ui.base.BaseAdapter

class ImageAdapter(
        parentLifecycle: Lifecycle,
        image_urls: ArrayList<String>
) : BaseAdapter<String, ImageItemViewHolder>(parentLifecycle, image_urls) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ImageItemViewHolder(parent)
}