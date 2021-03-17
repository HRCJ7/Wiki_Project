package com.example.mapapplication.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageQuery(
        @Expose
        @SerializedName("pages")
        val pages: Map<String, ImageInfo>
)