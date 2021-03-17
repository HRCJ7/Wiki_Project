package com.example.mapapplication.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeoSearch(
        @Expose
        @SerializedName("pageid")
        val pageId: Int,

        @Expose
        @SerializedName("ns")
        val ns: Int,

        @Expose
        @SerializedName("title")
        val title: String,

        @Expose
        @SerializedName("lat")
        val lat: Double,

        @Expose
        @SerializedName("lon")
        val lon: Double,

        @Expose
        @SerializedName("dist")
        val dist: Double,

        @Expose
        @SerializedName("primary")
        val primary: String
) : Parcelable