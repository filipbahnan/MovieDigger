package com.project.moviedigger.controllers

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.project.moviedigger.R
import com.project.moviedigger.models.Movie
import com.project.moviedigger.views.MovieFragment

class ReleasedAdapter(
    val layoutItem: Int
) : MovieAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolderReleased(
            // You may need to set this to fragment_upcoming.xml
            LayoutInflater.from(parent.context).inflate(
                layoutItem,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolderReleased) {
            holder.bind(movieList[position])
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class MovieViewHolderReleased(
        movieView: View
    ): MovieAdapter.MovieViewHolder(movieView) {

        private val movieImage: ImageView = movieView.findViewById(R.id.released_left_image)
        private val movieTitle: TextView = movieView.findViewById(R.id.released_left_title)
        private val movieGenres: TextView = movieView.findViewById(R.id.released_left_categories)
        private val movieRate: TextView = movieView.findViewById(R.id.released_left_rating)

        private val imageWidth: Int = 200
        private val movieBaseURL: String = "https://image.tmdb.org/t/p/w${imageWidth}"
        private var movieImageHolderURL: String = ""
        private var genres = ""

        init {
            movieImage.setOnClickListener { view ->
                openMovie(view, movie)
            }
            movieView.setOnClickListener(this)
        }

        override fun bind(movie: Movie) {
            this.movie = movie
            genres = movie.genreIds
            movieRate.text = "Rating: ${movie.voteAverage}"

            movieTitle.text = makeTitleFit(movie.title, "...", 12)
            movieGenres.text = this.genres
            movieImageHolderURL = "${movieBaseURL}${movie.posterPath}"

            if (movie.posterPath == "null") {
                movieImageHolderURL = "https://i.ibb.co/ThFnWc5/No-Picture.jpg"
            }

            loadImage(movieImageHolderURL, movieImage)
        }

        private fun openMovie(view: View, movie: Movie?) {
            val activity: AppCompatActivity = view.context as AppCompatActivity
            val movieItem = MovieFragment(movie, this.genres)

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

        override fun onClick(view: View?) {
            Toast.makeText(view?.context, "Click on a movie picture!", Toast.LENGTH_SHORT).show()
        }

        private fun makeTitleFit(title: String, pattern: String, length: Int): String {
            val short: String
            return if (title.length > length) {
                short = "${title.substring(0, length)}${pattern}"
                return short
            } else {
                title
            }
        }
    }

}