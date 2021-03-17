package com.example.mapapplication.data.model.google.directions

import com.google.gson.annotations.SerializedName

data class OverviewPolyline(@SerializedName("points")
                            val points: String = "")