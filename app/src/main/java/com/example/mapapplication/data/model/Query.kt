package com.example.mapapplication.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Query(@Expose
                 @SerializedName("geosearch")
                 val geoSearch: MutableList<GeoSearch>?)