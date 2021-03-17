package com.example.mapapplication.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Thumbnail(
        @Expose
        @SerializedName("height")
        val height: Int,

        @Expose
        @SerializedName("width")
        val width: Int,

        @SerializedName("source")
        @Expose
        val source: String
)