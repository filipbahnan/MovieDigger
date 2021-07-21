package com.project.moviedigger.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.project.moviedigger.MainActivity
import com.project.moviedigger.R
import com.project.moviedigger.controllers.ReleasedAdapter
import com.project.moviedigger.interfaces.MovieDbApi
import com.project.moviedigger.models.*
import com.project.moviedigger.networking.MovieDbFetch


class ReleasedFragment : Fragment() {
    private lateinit var recyclerViewReleased: RecyclerView
    private lateinit var movieAdapter: ReleasedAdapter
    private lateinit var movieDbFetch: MovieDbFetch

    private lateinit var btnRating: Button
    private lateinit var btnCategory: Button
    private lateinit var popupRating: PopupList
    private lateinit var popupCategory: PopupList
    private lateinit var theInterface: MovieDbApi
    private lateinit var resultFromCategory: PopupCategory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_released, container, false)
        recyclerViewReleased = view.findViewById(R.id.recycler_view_released)

        btnRating = view.findViewById(R.id.button_rating)
        btnCategory = view.findViewById(R.id.button_category)

        initRecyclerViewReleased(container?.context)

        theInterface = MovieDbApi.createService()
        movieDbFetch = MovieDbFetch(movieAdapter)

        initDropdownButtons()


        movieDbFetch.fetchMovieList(theInterface.listOfReleasedNonRegion())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).nav.menu.getItem(1).isChecked = true
        setupRefresh(view)
    }

    private fun setupRefresh(view: View) {
        val refresh: SwipeRefreshLayout = view.findViewById(R.id.released_refresh)
        refresh.setOnRefreshListener {
            movieAdapter.clearListOfMovies()

            theInterface = MovieDbApi.createService()


            resultFromCategory = PopupCategory(btnCategory, movieAdapter)
            movieDbFetch.fetchMovieList(theInterface.listOfReleasedSorted(PopupCategory.categoryResult, PopupRating.popupResult))
            refresh.isRefreshing = false
        }
    }

    private fun initRecyclerViewReleased(context: Context?) {
        recyclerViewReleased.apply {
            layoutManager = GridLayoutManager(context, 2)
            movieAdapter = ReleasedAdapter(R.layout.layout_released_movie_card)
            movieAdapter.stateRestorationPolicy = RecyclerView.Adapter
                .StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = movieAdapter
        }
    }

    private fun initDropdownButtons() {
        popupRating = PopupRating(btnRating, movieAdapter)
        btnRating.setOnClickListener { viewRating: View ->
            popupRating.show(viewRating, R.menu.popup_rating)
        }

        popupCategory = PopupCategory(btnCategory, movieAdapter)
        btnCategory.setOnClickListener { viewCategory: View ->
            popupCategory.show(viewCategory, R.menu.popup_category)
        }
    }

}
