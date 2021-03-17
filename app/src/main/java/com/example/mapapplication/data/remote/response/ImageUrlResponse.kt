package com.example.mapapplication.data.remote.response

import com.example.mapapplication.data.model.ImageQuery
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageUrlResponse(
        @Expose
        @SerializedName("batchcomplete")
        var batchComplete: String,

        @Expose
        @SerializedName("query")
        val query: ImageQuery
)