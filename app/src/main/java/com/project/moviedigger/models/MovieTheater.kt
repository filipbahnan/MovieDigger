package com.project.moviedigger.models

import com.google.gson.annotations.SerializedName

data class MovieTheater (
    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lng")
    val lng: Double,

    @SerializedName("name")
    val name: String
)