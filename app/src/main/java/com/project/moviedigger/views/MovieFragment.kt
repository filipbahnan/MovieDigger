package com.project.moviedigger.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.project.moviedigger.R
import com.project.moviedigger.models.Movie
import com.squareup.picasso.Picasso

class MovieFragment(
        private val movie: Movie?,
        private val genres: String?
) : Fragment() {
    private lateinit var title: TextView
    private lateinit var image: ImageView
    private lateinit var releaseDate: TextView
    private lateinit var rating: TextView
    private lateinit var genresTextView: TextView
    private lateinit var overview: TextView
    private lateinit var runtime: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_item, container, false)
        title = view.findViewById(R.id.movie_item_title)
        image = view.findViewById(R.id.movie_item_image)
        releaseDate = view.findViewById(R.id.movie_item_release_date)
        rating = view.findViewById(R.id.movie_item_rating)
        genresTextView = view.findViewById(R.id.movie_item_genres)
        overview = view.findViewById(R.id.movie_item_overview)
        runtime = view.findViewById(R.id.movie_item_runtime)

        val delimeter = " | "
        title.text = movie?.title
        releaseDate.text = "${movie?.releaseDate}${delimeter}"
        rating.text = "${movie?.voteAverage}/10${delimeter}"
        genresTextView.text = genres
        overview.text = movie?.overview

        if (movie?.runtime == "0") {
            runtime.text = "Playtime unknown | "
        }
        else {
            runtime.text = "${movie?.runtime} min | "
        }

        val imageWidth = 500
        val baseUrl = "https://image.tmdb.org/t/p/w${imageWidth}"
        var movieImageUrl = "${baseUrl}${movie?.posterPath}"

        if (movie?.posterPath == "null") {
            movieImageUrl = "https://thumbs.dreamstime.com/b/no-image-available-icon-photo-camera-flat-vector-illustration-132483141.jpg"
        }

        Picasso.get()
                .load(movieImageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(image)

        return view
    }
}