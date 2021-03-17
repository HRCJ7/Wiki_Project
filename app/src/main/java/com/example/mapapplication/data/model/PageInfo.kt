package com.example.mapapplication.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PageInfo (
        @Expose
        @SerializedName("pageid")
        val pageid: Int,

        @Expose
        @SerializedName("ns")
        val ns: Int,

        @SerializedName("title")
        @Expose
        val title: String,

        @SerializedName("contentmodel")
        @Expose
        val contentmodel: String,

        @SerializedName("pagelanguage")
        @Expose
        val pagelanguage: String,

        @SerializedName("pagelanguagehtmlcode")
        @Expose
        val pagelanguagehtmlcode: String,

        @SerializedName("pagelanguagedir")
        @Expose
        val pagelanguagedir: String,

        @SerializedName("touched")
        @Expose
        val touched: String,

        @SerializedName("lastrevid")
        @Expose
        val lastrevid: Int,

        @SerializedName("length")
        @Expose
        val length: Int,

        @SerializedName("fullurl")
        @Expose
        val fullurl: String,

        @SerializedName("editurl")
        @Expose
        val editurl: String,

        @SerializedName("canonicalurl")
        @Expose
        val canonicalurl: String
)