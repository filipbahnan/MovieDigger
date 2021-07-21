package com.project.moviedigger.networking

import com.project.moviedigger.controllers.MovieAdapter
import com.project.moviedigger.interfaces.MovieDbApi
import com.project.moviedigger.models.Movie
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDbFetch(
        val movieAdapter: MovieAdapter
) {

    fun fetchMovieList(responseBody: Call<ResponseBody>) {
        val callMovie: Call<ResponseBody> = responseBody
        callMovie.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val json = JSONArray(response.body()!!.string())
                val movieList: ArrayList<Movie> = ArrayList<Movie>()
                for (i in 0 until json.length()) {
                    movieList.add(Movie(
                            json.getJSONObject(i).getString("genre_ids"),
                            json.getJSONObject(i).getInt("id"),
                            json.getJSONObject(i).getString("original_language"),
                            json.getJSONObject(i).getString("overview"),
                            json.getJSONObject(i).getDouble("popularity"),
                            json.getJSONObject(i).getString("poster_path"),
                            json.getJSONObject(i).getString("release_date"),
                            json.getJSONObject(i).getString("runtime"),
                            json.getJSONObject(i).getString("title"),
                            json.getJSONObject(i).getDouble("vote_average")
                    ))
                }

                movieAdapter.setListOfMovies(movieList)
                movieAdapter.notifyDataSetChanged()
            }
        })
    }
}