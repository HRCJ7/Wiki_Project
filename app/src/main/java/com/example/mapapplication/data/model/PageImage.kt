package com.example.mapapplication.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PageImage(
        @Expose
        @SerializedName("ns")
        val ns: Int,

        @Expose
        @SerializedName("title")
        val title: String
)