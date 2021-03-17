package com.example.mapapplication.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArticleQuery(
        @Expose
        @SerializedName("pages")
        val pages: Map<String, Page>
)
