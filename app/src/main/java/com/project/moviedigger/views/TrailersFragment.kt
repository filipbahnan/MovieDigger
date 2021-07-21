package com.project.moviedigger.views

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.moviedigger.MainActivity
import com.project.moviedigger.R
import com.project.moviedigger.controllers.TrailerAdapter
import com.project.moviedigger.models.YoutubeVideo
import com.project.moviedigger.interfaces.YoutubeApiService
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.project.moviedigger.interfaces.GoogleMapApiService
import okhttp3.internal.notifyAll
import kotlin.collections.ArrayList


class TrailersFragment() : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var youtubeApiService: YoutubeApiService
    private lateinit var trailerAdapter: TrailerAdapter
    private lateinit var dataset: ArrayList<YoutubeVideo>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trailers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).nav.menu.getItem(2).isChecked = true
        setupRecyclerView(view)
        setupRefresh(view)
        youtubeApiService = YoutubeApiService.createService()
        generateData()
    }

    private fun setupRefresh(view: View) {
        val refresh: SwipeRefreshLayout = view.findViewById(R.id.trailer_refresh)
        refresh.setOnRefreshListener {
            dataset.clear()
            trailerAdapter.notifyItemRangeRemoved(0, trailerAdapter.itemCount -1)
            generateData()
            refresh.isRefreshing = false
        }
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.videos)
        recyclerView.hasFixedSize()
        recyclerView.setItemViewCacheSize(40)
        layoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = this.layoutManager
        dataset = ArrayList<YoutubeVideo>()
        trailerAdapter = TrailerAdapter(dataset)
        recyclerView.adapter = trailerAdapter
    }

    private fun generateData()  {
        youtubeApiService.fetchNewTrailers().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val json = JSONArray(response.body()!!.string())
                for (i in 0 until json.length()) {
                    dataset.add(YoutubeVideo(
                        json.getJSONObject(i).getString("videoId"),
                        json.getJSONObject(i).getString("title"),
                        json.getJSONObject(i).getString("published")
                    ))
                }
                trailerAdapter.notifyDataSetChanged()
            }
        })
    }
}