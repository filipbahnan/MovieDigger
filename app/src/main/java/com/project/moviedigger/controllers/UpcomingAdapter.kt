package com.project.moviedigger.controllers

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.project.moviedigger.R
import com.project.moviedigger.models.Movie
import com.project.moviedigger.views.MovieFragment

class UpcomingAdapter(
        val layoutItem: Int
) : MovieAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(
                // You may need to set this to fragment_upcoming.xml
                LayoutInflater.from(parent.context).inflate(
                        layoutItem,
                        parent,
            false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            // OBS! You send this listOfGenres multiple times to the function bind (N times)
            // Not an optimal solution :'(
            holder.bind(movieList[position])
        }

    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class MovieViewHolder(
            movieView: View
    ): MovieAdapter.MovieViewHolder(movieView) {
        private val movieImage: ImageView = movieView.findViewById(R.id.upcoming_image)
        private val movieTitle: TextView = itemView.findViewById(R.id.upcoming_title)
        private val movieGenres: TextView = itemView.findViewById(R.id.upcoming_genres)
        private val movieReleaseDate: TextView = itemView.findViewById(R.id.upcoming_release_date)

        private val imageWidth: Int = 200
        private val movieBaseURL: String = "https://image.tmdb.org/t/p/w${imageWidth}"
        private var movieImageHolderURL: String = ""
        private var genres = ""

        // Make the movie post clickable (this is not the best way at all to do this)
        init {
            movieView.setOnClickListener(this)
        }

        override fun bind(movie: Movie) {
            this.movie = movie // Important binding for movie content
            this.genres = movie.genreIds

            movieTitle.text = movie.title
            movieGenres.text = this.genres
            movieReleaseDate.text = movie.releaseDate
            movieImageHolderURL = "${movieBaseURL}${movie.posterPath}"

            if (movie.posterPath == "null") {
                movieImageHolderURL = "https://i.ibb.co/ThFnWc5/No-Picture.jpg"
            }

            loadImage(movieImageHolderURL, movieImage)
        }

        override fun onClick(view: View?) {
            val activity: AppCompatActivity = view?.context as AppCompatActivity
            val movieItem = MovieFragment(movie, genres)
            activity.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.animator.slide_in_from_right,
                    0,
                    0,
                    R.animator.slide_out_from_left
                )
                .replace(R.id.fragment_container, movieItem)
                .addToBackStack(null) // Make back press go back to list, and not exit app
                .commit()
        }
    }
}