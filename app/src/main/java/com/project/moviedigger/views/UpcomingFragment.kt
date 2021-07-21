package com.project.moviedigger.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.project.moviedigger.MainActivity
import com.project.moviedigger.interfaces.MovieDbApi
import com.project.moviedigger.R
import com.project.moviedigger.controllers.UpcomingAdapter
import com.project.moviedigger.models.*
import com.project.moviedigger.networking.MovieDbFetch
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TAG = "MainActivity"
class UpcomingFragment : Fragment() {
    private lateinit var recyclerViewUpcoming: RecyclerView
    private lateinit var movieAdapter: UpcomingAdapter
    private lateinit var movieDbFetch: MovieDbFetch
    private val theInterface: MovieDbApi? = null
    private lateinit var userPrefs: SharedPreferences

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming, container, false)
        recyclerViewUpcoming = view.findViewById(R.id.recycler_view_upcoming)

        initRecyclerViewUpcoming(container?.context)

        val theInterface: MovieDbApi = MovieDbApi.createService()
        movieDbFetch = MovieDbFetch(movieAdapter)
        movieDbFetch.fetchMovieList(theInterface.listOfUpcomingNonRegion())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).nav.menu.getItem(0).isChecked = true
        setupRefresh(view)
    }

    private fun setupRefresh(view: View) {
        val refresh: SwipeRefreshLayout = view.findViewById(R.id.upcoming_refresh)
        refresh.setOnRefreshListener {
            movieAdapter.clearListOfMovies()
            val theInterface: MovieDbApi = MovieDbApi.createService()

            movieDbFetch.fetchMovieList(theInterface.listOfUpcomingNonRegion())
            refresh.isRefreshing = false
        }
    }

    private fun initRecyclerViewUpcoming(context: Context?) {
        recyclerViewUpcoming.apply {
            layoutManager = LinearLayoutManager(context)
            movieAdapter = UpcomingAdapter(R.layout.layout_upcoming_movie_card)
            // Keep the scroll position (do not go back to top of list)
            movieAdapter.stateRestorationPolicy = RecyclerView.Adapter
                .StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = movieAdapter
        }
    }
}