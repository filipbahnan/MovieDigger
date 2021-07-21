package com.project.moviedigger.models
import com.google.gson.annotations.SerializedName

data class YoutubeVideo(
        @SerializedName("videoId")
        var videoId: String,

        @SerializedName("title")
        var title: String,

        @SerializedName("publishedAt")
        var publishedAt: String
)