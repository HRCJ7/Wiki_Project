package com.example.mapapplication.data.model.google.nearbySearch

import com.google.gson.annotations.SerializedName

data class Viewport(@SerializedName("southwest")
                    val southwest: Southwest,
                    @SerializedName("northeast")
                    val northeast: Northeast)