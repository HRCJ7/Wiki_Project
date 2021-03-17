package com.example.mapapplication.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageInfo(

        @Expose
        @SerializedName("ns")
        val ns: Int,

        @SerializedName("title")
        @Expose
        val title: String,

        @SerializedName("missing")
        @Expose
        val missing: String,

        @SerializedName("known")
        @Expose
        val known: String,

        @SerializedName("pageimage")
        @Expose
        val pageimage: String,

        @SerializedName("thumbnail")
        @Expose
        val thumbnail: Thumbnail
)