package com.example.mapapplication.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PageQuery (
        @Expose
        @SerializedName("pages")
        val pages: Map<String, PageInfo>
)