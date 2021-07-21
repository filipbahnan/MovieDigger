package com.project.moviedigger.views

import android.util.Log
import android.view.MenuItem
import android.widget.Button
import com.project.moviedigger.R
import com.project.moviedigger.controllers.MovieAdapter
import com.project.moviedigger.interfaces.MovieDbApi
import com.project.moviedigger.networking.MovieDbFetch

class PopupRating(
        private val button: Button, private val movieAdapter: MovieAdapter
) : PopupList() {
    private lateinit var movieDbFetch: MovieDbFetch
    private lateinit var theInterface: MovieDbApi
    companion object { var popupResult: String = "None" }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        theInterface = MovieDbApi.createService()
        movieDbFetch = MovieDbFetch(movieAdapter)

        val none = view.context.getString(R.string.rating_none)
        val lowest = view.context.getString(R.string.rating_lowest)
        val highest = view.context.getString(R.string.rating_highest)

        var canClick = true
        when (item?.itemId) {
            R.id.rating_none -> {
                result = none
                popupResult = none
            }
            R.id.rating_lowest -> {
                result = lowest
                popupResult = lowest
            }
            R.id.rating_highest -> {
                result = highest
                popupResult = highest
            }
            else -> {
                canClick = false
            }
        }

        button.text = result + dropdownArrow
        movieDbFetch.fetchMovieList(theInterface.listOfReleasedSorted(PopupCategory.categoryResult, result))
        return canClick
    }
}
