package com.project.moviedigger.views

import android.util.Log
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Button
import com.project.moviedigger.R
import com.project.moviedigger.controllers.MovieAdapter
import com.project.moviedigger.interfaces.MovieDbApi
import com.project.moviedigger.networking.MovieDbFetch

class PopupCategory(
        private val button: Button, private val movieAdapter: MovieAdapter
) : PopupList() {
    private lateinit var movieDbFetch: MovieDbFetch
    private lateinit var theInterface: MovieDbApi
    companion object {var categoryResult: String = "All"}

    override fun onMenuItemClick(item: MenuItem): Boolean {
        theInterface = MovieDbApi.createService()
        movieDbFetch = MovieDbFetch(movieAdapter)
        var canClick = true
        when (item?.itemId) {
            R.id.category_all -> {result = view.context.getString(R.string.all); categoryResult = view.context.getString(R.string.all)}
            R.id.category_action -> {result = view.context.getString(R.string.action); categoryResult = view.context.getString(R.string.action)}
            R.id.category_adventure -> {result = view.context.getString(R.string.adventure); categoryResult = view.context.getString(R.string.adventure)}
            R.id.category_comedy -> {result = view.context.getString(R.string.comedy); categoryResult = view.context.getString(R.string.comedy)}
            R.id.category_crime -> {result = view.context.getString(R.string.crime); categoryResult = view.context.getString(R.string.crime)}
            R.id.category_documentary -> {result = view.context.getString(R.string.documentary); categoryResult = view.context.getString(R.string.documentary)}
            R.id.category_drama -> {result = view.context.getString(R.string.drama); categoryResult = view.context.getString(R.string.drama)}
            R.id.category_family -> {result = view.context.getString(R.string.family); categoryResult = view.context.getString(R.string.family)}
            R.id.category_fantasy -> {result = view.context.getString(R.string.fantasy); categoryResult = view.context.getString(R.string.fantasy)}
            R.id.category_history -> {result = view.context.getString(R.string.history); categoryResult = view.context.getString(R.string.history)}
            R.id.category_horror -> {result = view.context.getString(R.string.horror); categoryResult = view.context.getString(R.string.horror)}
            R.id.category_music -> {result = view.context.getString(R.string.music); categoryResult = view.context.getString(R.string.music)}
            R.id.category_mystery -> {result = view.context.getString(R.string.mystery); categoryResult = view.context.getString(R.string.mystery)}
            R.id.category_romance -> {result = view.context.getString(R.string.romance); categoryResult = view.context.getString(R.string.romance)}
            R.id.category_science_fiction -> {result = view.context.getString(R.string.science_fiction); categoryResult = view.context.getString(R.string.science_fiction)}
            R.id.category_tv_movie -> {result = view.context.getString(R.string.tv_movie); categoryResult = view.context.getString(R.string.tv_movie)}
            R.id.category_thriller -> {result = view.context.getString(R.string.thriller); categoryResult = view.context.getString(R.string.thriller)}
            R.id.category_war_western -> {result = view.context.getString(R.string.war_western); categoryResult = view.context.getString(R.string.war_western)}
            else -> {
                canClick = false
            }
        }
        button.text = result + dropdownArrow
        movieDbFetch.fetchMovieList(theInterface.listOfReleasedSorted(result,PopupRating.popupResult))
        return canClick
    }
}