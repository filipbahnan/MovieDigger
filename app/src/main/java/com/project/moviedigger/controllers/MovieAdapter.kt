package com.project.moviedigger.controllers

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.project.moviedigger.R
import com.project.moviedigger.models.*
import com.squareup.picasso.Picasso

abstract class MovieAdapter(

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var movieList: MutableList<Movie> = mutableListOf()

    // Make sure to implement the class to hold the movie item in the list
    abstract class MovieViewHolder(
        movieView: View
    ): RecyclerView.ViewHolder(movieView), View.OnClickListener {
        protected var movie: Movie? = null

        // Must implement this function to bind content to the movie item
        abstract fun bind(movie: Movie)

        fun loadImage(url: String, image: ImageView) {
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(image)
        }
    }

    fun clearListOfMovies() {
        movieList.clear()
    }

    fun setListOfMovies(movieList: MutableList<Movie>) {
        this.movieList = movieList
    }
}