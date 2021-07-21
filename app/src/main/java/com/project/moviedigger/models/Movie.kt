package com.project.moviedigger.models

import com.google.gson.annotations.SerializedName

data class Movie(
        @SerializedName("genre_ids")
        val genreIds: String,

        @SerializedName("id")
        val id: Int,

        @SerializedName("original_language")
        val originalLanguage: String,

        @SerializedName("overview")
        val overview: String,

        @SerializedName("popularity")
        val popularity: Double,

        @SerializedName("poster_path")
        val posterPath: String,

        @SerializedName("release_date")
        val releaseDate: String,

        @SerializedName("runtime")
        val runtime: String,

        @SerializedName("title")
        val title: String,

        @SerializedName("vote_average")
        val voteAverage: Double
)